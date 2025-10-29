#!/bin/bash

# Branch Cleanup Script for SHIFAA Premium
# This script helps safely delete old and merged branches

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
DRY_RUN=false
FORCE=false
INTERACTIVE=true

# Parse arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --dry-run)
            DRY_RUN=true
            shift
            ;;
        --force)
            FORCE=true
            INTERACTIVE=false
            shift
            ;;
        --yes|-y)
            INTERACTIVE=false
            shift
            ;;
        --help|-h)
            echo "Branch Cleanup Script"
            echo ""
            echo "Usage: $0 [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  --dry-run    Show what would be deleted without actually deleting"
            echo "  --force      Delete all old branches without confirmation"
            echo "  --yes, -y    Skip confirmation prompts"
            echo "  --help, -h   Show this help message"
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            echo "Use --help for usage information"
            exit 1
            ;;
    esac
done

echo -e "${BLUE}================================${NC}"
echo -e "${BLUE}SHIFAA Premium - Branch Cleanup${NC}"
echo -e "${BLUE}================================${NC}"
echo ""

# Check if we're in a git repository
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo -e "${RED}Error: Not in a git repository${NC}"
    exit 1
fi

# Fetch latest remote information
echo -e "${YELLOW}Fetching latest remote information...${NC}"
git fetch --all --prune > /dev/null 2>&1

# Also update remote refs
git remote update origin --prune > /dev/null 2>&1

# Get current branch
CURRENT_BRANCH=$(git branch --show-current)
echo -e "Current branch: ${GREEN}${CURRENT_BRANCH}${NC}"
echo ""

# List of branches to potentially delete
# These are old AI assistant branches that are likely merged
BRANCHES_TO_CHECK=(
    "claude/android-emulator-issues-011CUUuv1ZKaDN3kM6Q9NzUG"
    "claude/system-patching-011CUW3dzhiuzpZ6CiAHmqkf"
    "compyle/app-deployment-ready"
)

# Branches to NEVER delete
PROTECTED_BRANCHES=(
    "master"
    "dev"
    "main"
)

echo -e "${YELLOW}Analyzing branches...${NC}"
echo ""

BRANCHES_TO_DELETE=()

for branch in "${BRANCHES_TO_CHECK[@]}"; do
    # Check if branch exists on remote using ls-remote
    if git ls-remote --heads origin "$branch" | grep -q "$branch"; then
        echo -e "${BLUE}Checking: ${branch}${NC}"
        
        # Fetch the specific branch if not already fetched
        git fetch origin "$branch:refs/remotes/origin/$branch" 2>/dev/null || true
        
        # Get branch info
        LAST_COMMIT=$(git log -1 --format="%ci - %s" "origin/$branch" 2>/dev/null || echo "N/A")
        echo -e "  Last commit: ${LAST_COMMIT}"
        
        # Check if merged into master
        if git merge-base --is-ancestor "origin/$branch" origin/master 2>/dev/null; then
            echo -e "  Status: ${GREEN}✓ Merged into master${NC}"
            BRANCHES_TO_DELETE+=("$branch")
        else
            # Check for unique commits
            UNIQUE_COMMITS=$(git rev-list --count origin/master..origin/$branch 2>/dev/null || echo "0")
            if [ "$UNIQUE_COMMITS" -eq "0" ]; then
                echo -e "  Status: ${GREEN}✓ No unique commits${NC}"
                BRANCHES_TO_DELETE+=("$branch")
            else
                echo -e "  Status: ${YELLOW}⚠ Has ${UNIQUE_COMMITS} unique commit(s)${NC}"
                echo -e "  ${YELLOW}Review before deleting!${NC}"
                
                if [ "$FORCE" = false ]; then
                    echo -e "  ${RED}Skipping (use --force to delete anyway)${NC}"
                else
                    BRANCHES_TO_DELETE+=("$branch")
                fi
            fi
        fi
        echo ""
    else
        echo -e "${BLUE}Branch not found: ${branch}${NC}"
        echo -e "  ${GREEN}✓ Already deleted or doesn't exist${NC}"
        echo ""
    fi
done

# Show summary
echo -e "${BLUE}================================${NC}"
echo -e "${BLUE}Summary${NC}"
echo -e "${BLUE}================================${NC}"
echo ""

if [ ${#BRANCHES_TO_DELETE[@]} -eq 0 ]; then
    echo -e "${GREEN}✓ No branches need to be deleted${NC}"
    echo -e "${GREEN}✓ Repository is clean!${NC}"
    exit 0
fi

echo -e "Branches to be deleted: ${RED}${#BRANCHES_TO_DELETE[@]}${NC}"
echo ""
for branch in "${BRANCHES_TO_DELETE[@]}"; do
    echo -e "  ${RED}✗ ${branch}${NC}"
done
echo ""

# Dry run mode
if [ "$DRY_RUN" = true ]; then
    echo -e "${YELLOW}[DRY RUN] No branches were actually deleted${NC}"
    echo -e "${YELLOW}Run without --dry-run to perform actual deletion${NC}"
    exit 0
fi

# Interactive confirmation
if [ "$INTERACTIVE" = true ]; then
    echo -e "${YELLOW}⚠ WARNING: This will permanently delete ${#BRANCHES_TO_DELETE[@]} branch(es)${NC}"
    echo -e "${YELLOW}Are you sure you want to continue? (yes/no)${NC}"
    read -r response
    
    if [[ ! "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
        echo -e "${BLUE}Cancelled. No branches were deleted.${NC}"
        exit 0
    fi
fi

# Delete branches
echo ""
echo -e "${YELLOW}Deleting branches...${NC}"
echo ""

DELETED_COUNT=0
FAILED_COUNT=0

for branch in "${BRANCHES_TO_DELETE[@]}"; do
    echo -e "Deleting: ${RED}${branch}${NC}"
    
    if git push origin --delete "$branch" 2>&1; then
        echo -e "  ${GREEN}✓ Deleted successfully${NC}"
        DELETED_COUNT=$((DELETED_COUNT + 1))
    else
        echo -e "  ${RED}✗ Failed to delete${NC}"
        FAILED_COUNT=$((FAILED_COUNT + 1))
    fi
    echo ""
done

# Clean up local tracking branches
echo -e "${YELLOW}Cleaning up local tracking branches...${NC}"
git fetch --prune > /dev/null 2>&1
echo -e "${GREEN}✓ Local tracking branches cleaned${NC}"
echo ""

# Final summary
echo -e "${BLUE}================================${NC}"
echo -e "${BLUE}Cleanup Complete${NC}"
echo -e "${BLUE}================================${NC}"
echo ""
echo -e "Branches deleted: ${GREEN}${DELETED_COUNT}${NC}"
if [ $FAILED_COUNT -gt 0 ]; then
    echo -e "Failed deletions: ${RED}${FAILED_COUNT}${NC}"
fi
echo ""

# Show remaining branches
echo -e "${YELLOW}Remaining branches:${NC}"
git branch -r | grep -v 'HEAD' | sed 's/origin\///' | while read -r branch; do
    if [[ " ${PROTECTED_BRANCHES[@]} " =~ " ${branch} " ]]; then
        echo -e "  ${GREEN}✓ ${branch}${NC} (protected)"
    else
        echo -e "  ${BLUE}• ${branch}${NC}"
    fi
done
echo ""

if [ $DELETED_COUNT -gt 0 ]; then
    echo -e "${GREEN}✓ Branch cleanup successful!${NC}"
    echo -e "${BLUE}Your repository is now cleaner and less confusing.${NC}"
else
    echo -e "${YELLOW}No branches were deleted.${NC}"
fi
