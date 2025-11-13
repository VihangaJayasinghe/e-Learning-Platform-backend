Branching rules (must follow)
- Do NOT develop on main.
- Switch to the dev branch for development work:
  - git fetch
  - git checkout dev
  - git pull
- Create a new branch from dev for each task:
  - git checkout -b <branch-name>   (while on dev)

Example branch name patterns
- feat/<discribe-feature>
  - e.g. feat/add_student_CRUD
- bug/<discribe-the-bug-you-aim-to-fix>
  - e.g. bug/videos_not_loading
...

Basic workflow
1. Start from dev:
   - git fetch
   - git checkout dev
   - git pull
2. Create branch:
   - git checkout -b feature/123-short-desc
3. Work, commit, push:
   - git add .
   - git commit -m "brief: description"
   - git push -u origin feature/123-short-desc
4. Open a PR targeting dev (not main). Wait for review, then merge into dev.
5. Only release/merge to main when preparing a stable release (follow your release process).

Notes
- Keep commits small and focused.
- PRs should target dev.
- See HELP.md for any repo-specific details.
