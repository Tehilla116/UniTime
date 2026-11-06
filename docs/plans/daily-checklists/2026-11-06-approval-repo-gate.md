# Friday 6 November 2026 — Approval, repo, and scope gate

## Objective
Confirm the lecturer’s approval and resolve the submission-blocking repository/source-root gap before implementation.

## Immediate checklist
- [ ] Check whether written lecturer **Approved** exists. If not, contact the lecturer with UniTime proposal and request next steps. **No implementation before approval.**
- [ ] Create/identify the single shared GitHub repository; invite all five; verify each member can clone and make a branch/commit.
- [ ] Agree the actual code root: current Android source is `/Users/tehillachiwama/Year 3/CampusConnectMVP/`; canonical `projects/UniTime/` currently contains proposal and plans only. Decide how to bring source into the shared repo without losing work.
- [ ] Verify emulator/device and Android Studio run locally.
- [ ] Agree `ClassEntry`: ID, name, weekday, start/end, optional venue. Agree validation, overlap warning, and reminder limits.
- [ ] Assign a same-day availability window and final video/peer-rating/submission link owners.
- [ ] Capture real successful commands and repo URL; don't invent either.

## Owners
Tehilla coordinates approval, Room data contract, and repo-root decision; Frank confirms form fields and clone flow; Bright defines schedule/overlap examples; Cheela checks navigation/device; Mwenya confirms notification feasibility and permission testing.

## Gate and evidence
Evidence: lecturer decision, working shared repo/access for 5, real source root, emulator, agreed model contract. If approval is absent, **stop coding and escalate**; update timeline after lecturer response.