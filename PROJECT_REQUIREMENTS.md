# Project Requirements: Command Based Turing Tape Simulator

**Source:** Automata Theory individual project requirements, `Turing_Tape_Simulator_Project_Requirements_Final.pdf`, pp. 1–4. This document distinguishes mandatory requirements, permitted choices, and open details. The professor’s instructions take precedence over implementation preferences.

## 1. Project overview and objectives (p. 1)

Build an individual simulator that reads a small, assembly-like program and executes it deterministically on a Turing-style tape. The project starts with the basic Turing machine lesson on October 15, 2026, allowing about six and a half weeks for development. The submission deadline is **November 29, 2026, at 11:59 PM Philippine time (UTC+8)**.

Learning objectives:

- Represent a machine configuration with tape contents, head position, and program counter (the instruction position).
- Implement deterministic execution, branching, and halting, and trace how each command changes the configuration.
- Test normal and boundary behavior and explain why reaching the execution limit does not prove that a program never halts.
- Demonstrate individual understanding by making and testing a small unseen change under supervision.

## 2. Tape and input specifications (pp. 1–2)

| ID | Mandatory requirement |
| --- | --- |
| T1 | The tape starts at index `0` and extends to the right without a fixed limit. Each cell holds exactly one non-whitespace character. `_` is blank; unvisited cells read as `_`. Symbols are case-sensitive (`A` and `a` differ). |
| T2 | Accept a tape input as text, including an explicitly empty input. Reject input containing whitespace. Load the first symbol at index `0` and the rest consecutively to the right. Treat `_` in the input as a blank cell; all cells outside the input are blank. |
| T3 | Initially place the head at index `0`, even for empty input, point the program counter at the first executable instruction, and start the step count at `0`. |

**Permitted storage choices:** A sparse map or a dynamically expanding sequence is acceptable.

## 3. Program format and validation (p. 2)

| ID | Mandatory requirement |
| --- | --- |
| P1 | Load a plain-text program file. Use one instruction per line; an optional label appears before the instruction on the same line and ends with a colon (e.g. `scan: RIGHT`). Reject a label-only line. |
| P2 | Require uppercase commands and separate tokens with spaces or tabs. Ignore leading and trailing whitespace and blank lines. Each symbol argument is a single token containing exactly one non-whitespace character, written directly without quoted-string syntax. |
| P3 | Labels are case-sensitive, must match `[A-Za-z][A-Za-z0-9_]*`, and must be unique. Every jump target must exist. Forward and backward jumps are allowed. |
| P4 | Validate the whole program before executing any instruction. Reject empty programs, unknown commands, wrong argument counts, invalid symbol tokens, malformed labels, duplicate labels, and undefined targets. Report the source line and a useful reason; do not start execution after a validation error. |
| P5 | Comments are optional. If comments are not supported, reject them rather than silently skipping them. |

## 4. Command semantics and execution rules (pp. 1–2)

| Command | Tape and head effects | Program counter or status |
| --- | --- | --- |
| `LEFT` | Move the head one cell left, or leave it at `0` if already there. No tape change. | Continue to the next instruction. |
| `RIGHT` | Move the head one cell right. No tape change. | Continue to the next instruction. |
| `WRITE s` | Replace the current cell with `s`; do not move the head or insert or shift any cells. Writing `_` erases the current cell. | Continue to the next instruction. |
| `IF s GOTO label` | Compare the current symbol with `s` case-sensitively. Do not change the tape or head. | Jump to the named instruction if equal; otherwise continue to the next instruction. |
| `GOTO label` | Do not change the tape or head. | Jump unconditionally to the named instruction. |
| `HALT` | Do not change the tape or head. | Stop successfully with `HALTED` and leave the program counter on the executed `HALT`. |

| ID | Mandatory requirement |
| --- | --- |
| E1 | Count each executed instruction as one step, including `LEFT` at `0`, `IF` on either branch, `GOTO`, and `HALT`. Labels and blank lines do not count. |
| E2 | Falling past the last instruction without executing `HALT` is an `ERROR`, not a successful halt. Stop and report the runtime error, preserving the final tape, head position, and step count. |
| E3 | The step limit must be a positive integer and defaults to `10,000`. Apply it to total instructions executed since reset, not separately to each `Step` or `Run`. |
| E4 | After the last permitted step, report `LIMIT REACHED` only if the machine would otherwise still be running. If that step executes `HALT` or causes a runtime error, report `HALTED` or `ERROR` instead. `LIMIT REACHED` means the execution was bounded, not that the program can never halt. |

## 5. Controls and observable output (p. 2)

| ID | Mandatory requirement |
| --- | --- |
| U1 | Provide `Step` (execute exactly one instruction) and `Run` (continue until `HALT`, a runtime error, or the step limit). After any terminal status, require `Reset` before executing again. |
| U2 | Provide `Reset` to restore the loaded program and original input tape, set the head to `0`, return the program counter to the first executable instruction, and set the step count to `0`. |
| U3 | Display the configuration initially, after every `Step`, and when `Run` stops. Show a tape window with cell indices and a visible head marker, the head index, the next instruction or terminal instruction/status, and the total step count. |
| U4 | Do not hide changes to nonblank cells: either show all nonblank cells plus the head or provide a documented way to inspect any tape position. |
| U5 | Document program-file and tape-input handling, including how to supply an explicitly empty tape. No specific command-line flag names are required. |

A command-line interface is sufficient. **Optional features:** A graphical interface, animation, database, and web deployment earn no separate credit. Per-step output during `Run` is also optional.

## 6. Technical constraints and academic integrity (p. 3)

- Implement the interpreter directly in **Java or Python**, the languages taught before this subject. Simple token splitting, standard string operations, and standard parsing libraries are allowed.
- Implement the simulator’s own tape storage, label resolution, program counter, and command execution. Do not use an off-the-shelf Turing machine simulator or interpreter engine for core execution. Do not translate the program into host-language code and execute it with `eval`, `exec`, or an equivalent facility.
- Keep parsing, machine state, and execution sufficiently separate to make and test a small command or behavior change.
- Development is individual. Do not copy projects from classmates or previous batches. Students remain responsible for understanding, testing, and modifying all code.
- AI explanations, debugging help, code suggestions, and code generation are allowed during development. If used, submit a short disclosure naming the tool, its purpose, and the affected components. Exhaustive prompt logs are not required. These permissions do not apply to the supervised assessment.

## 7. Expected results and acceptance criteria (pp. 2–3)

### Professor’s example: append `1` at the first blank

For this example only, the input contains only `0` and `1`. The simulator itself must also handle nonbinary symbols.

```text
scan: IF _ GOTO append
RIGHT
GOTO scan
append: WRITE 1
HALT
```

| Input | Final status | Expected tape | Final head index | Total steps |
| --- | --- | --- | ---: | ---: |
| `101` | `HALTED` | Cells `0`–`3` contain `1011` | 3 | 12 |
| Explicitly empty | `HALTED` | Cell `0` contains `1` | 0 | 3 |

### Acceptance evidence

The simulator must meet Sections 2–6 and demonstrate all required test cases in the checklist below. Automated tests and documented manual tests are both acceptable. For **each** test, record the program, input, expected outcome, actual outcome, and pass/fail result. The take-home requirements and test record prepare the frozen assessment baseline; satisfying them does not replace the supervised assessment or award project points by itself.

## 8. Submission requirements (p. 3)

Submit **one ZIP** by the stated deadline through the instructor-designated channel. Include:

- Complete editable source code and any required dependency list. Do not include passwords, API keys, or unnecessary build folders.
- A short README with the language and version, setup and run commands, controls, file and input formats, known limitations, and required offline dependencies.
- Sample program files and the required test record.
- A brief design explanation covering machine state and execution, and the short AI-assistance disclosure if applicable.

Keep the submitted archive unchanged as the **frozen assessment baseline**. Perform the supervised change in a separate copy and retain both versions.

## 9. Supervised individual assessment (p. 4)

This assessment determines **100% of the project grade**, not the entire course grade. The take-home simulator is preparation and the assessment baseline; polish or submission alone earns no project points.

Each student receives one bounded modification with clear acceptance tests. Suitable task families include adding a simple command, adjusting a specified execution rule, or extending a validation rule. The modification must require understanding the machine and making executable code changes; changing interface colors, interface labels, or documentation alone is insufficient.

Work individually and offline, with no AI tools, internet access, or messaging. Start from the frozen submitted source in a separate copy. Complete a short unseen execution trace first, then record an approach, implement the change, run tests, and explain the result in writing. Coherent partial work can receive partial credit even without a working final modification.

| Criterion | Points | Evidence and partial credit |
| --- | ---: | --- |
| Approach and understanding | 20 | Accurately trace the unseen program and identify affected state, execution path, and relevant edge cases. A sound plan can earn partial credit even if implementation is incomplete. |
| Implementation | 45 | Implement the requested semantics while preserving unaffected behavior. Correct individual parts can earn partial credit even if the whole application does not run. |
| Testing and debugging | 25 | Run relevant normal, boundary, and regression tests, compare actual and expected results, and use failures to guide fixes. Well-designed tests and accurate diagnosis can earn partial credit. |
| Written explanation | 10 | Explain the changed code, effects on tape, head, and program counter, and any remaining defect. Accurate reasoning must match the submitted work. |
| **Total** | **100** | |

Submit the modified source, tests, and short written explanation from the supervised session. Equivalent task variants use the same rubric. A brief oral follow-up may be used only if the submitted evidence is unclear or inconsistent, and only to clarify the same rubric criteria.

## 10. Open details and permitted decisions

- **Pending instructor announcement:** Submission channel, lab date, lab duration, and permitted offline reference materials. The specific modification and its acceptance tests are assigned during the assessment process, not specified in the project brief.
- **Implementation choices:** Java or Python, language version, interface and flag names, file-loading and input-entry conventions, and the tape display/inspection approach. Document the choices where required; do not treat a particular choice as professor-mandated.
- **Syntax details not specified:** Comment syntax if implemented; spacing immediately around the label colon beyond the general token rules; file encoding and precise Unicode character/whitespace handling. Preserve the source’s single-character symbol rule, uppercase commands, and ASCII label pattern; the alphabet is not limited to binary symbols.
- **Diagnostic details not specified:** The source requires a source line and useful reason for validation failures but gives no line-number convention for a program with no source lines. It does not prescribe diagnostic wording or require reporting all failures in one pass.
- **Conflicts:** No conflicting requirements were identified in the source.

## 11. Implementation and delivery checklist

All items start unchecked. Mark an item complete only after verifying it. The IDs connect tasks to the requirements above; optional features are not completion gates.

### Tape and initialization

- [ ] `T1` Store one case-sensitive, non-whitespace character per cell.
- [ ] `T1` Read untouched cells as `_`.
- [ ] `T1` Grow rightward beyond the original input without a fixed tape boundary.
- [ ] `T2` Load text input consecutively from index `0`, interpreting `_` as blank.
- [ ] `T2` Accept an explicitly empty tape.
- [ ] `T2` Reject input containing whitespace.
- [ ] `T3` Start at head index `0`, at the first executable instruction, with zero steps, including on an empty tape.

### Program loading and validation

- [ ] `P1` Load a plain-text program file.
- [ ] `P1` Parse one instruction per line, with or without a same-line label.
- [ ] `P2` Parse space- or tab-separated tokens, ignoring leading/trailing whitespace and blank lines.
- [ ] `P2` Enforce uppercase commands and direct, single-character symbol arguments without quoted strings.
- [ ] `P3` Resolve case-sensitive, unique labels matching the specified pattern, including forward and backward targets.
- [ ] `P4` Reject an empty program.
- [ ] `P4` Reject unknown commands, including incorrect command casing.
- [ ] `P4` Reject wrong argument counts.
- [ ] `P4` Reject invalid symbol tokens.
- [ ] `P1`, `P4` Reject label-only lines and malformed labels.
- [ ] `P4` Reject duplicate labels.
- [ ] `P4` Reject undefined jump targets.
- [ ] `P5` Reject comments if unsupported.
- [ ] `P4` Report a source line and useful reason for validation errors, before executing anything.

### Commands and execution

- [ ] Verify `LEFT` moves one cell left except at index `0`.
- [ ] Verify `RIGHT` moves one cell right.
- [ ] Verify `WRITE` replaces or erases one cell without moving the head or shifting cells.
- [ ] Verify `IF` jumps on a match and advances on a mismatch without changing tape or head.
- [ ] Verify `GOTO` jumps unconditionally without changing tape or head.
- [ ] Verify `HALT` stops successfully with tape and head unchanged and the program counter on `HALT`.
- [ ] `E1` Count one step per executed instruction and none for labels or blank lines.
- [ ] `E2` Report falling past the end as `ERROR` and preserve tape, head, and step count.
- [ ] `E3` Accept only a positive integer limit and use `10,000` by default.
- [ ] `E3` Apply the limit cumulatively across `Step` and `Run` since reset.
- [ ] `E4` Report `LIMIT REACHED` only if still running at the cutoff, with `HALTED` and `ERROR` taking precedence on the final step.

### Controls and display

- [ ] `U1` Provide `Step` that executes one instruction.
- [ ] `U1` Provide `Run` that stops at halt, error, or the limit.
- [ ] `U1` Block further execution after a terminal status until reset.
- [ ] `U2` Provide `Reset` that restores the loaded program, original tape, initial head and program counter, and zero steps.
- [ ] `U3` Display the configuration initially, after every `Step`, and when `Run` stops.
- [ ] `U3` Display indexed cells, a visible head marker, head index, next instruction or terminal instruction/status, and total steps.
- [ ] `U4` Show all nonblank cells plus the head, or document how to inspect any tape position.
- [ ] `U5` Document file loading and input handling, including explicit empty input.

### Implementation constraints

- [ ] Use Java or Python.
- [ ] Own the tape storage, label resolution, program counter, and command execution; use neither a ready-made execution engine nor host-language code evaluation.
- [ ] Keep parsing, state, and execution sufficiently separate for small, testable changes.
- [ ] Develop individually without copying classmates’ or previous batches’ projects, and be able to understand, test, and modify the code.

### Required test record

- [ ] Record program, input, expected outcome, actual outcome, and pass/fail for every test.
- [ ] Exercise every command (`LEFT`, `RIGHT`, `WRITE`, `IF`, `GOTO`, `HALT`).
- [ ] Exercise both true and false `IF` branches.
- [ ] Exercise a forward jump.
- [ ] Exercise a backward jump.
- [ ] Test immediate `HALT`.
- [ ] Test the example on `101`: `HALTED`, cells `0`–`3` equal `1011`, head `3`, steps `12`.
- [ ] Test the example on empty input: `HALTED`, cell `0` equals `1`, head `0`, steps `3`.
- [ ] Test `LEFT` at `0`: head does not move and one step is counted.
- [ ] Test moving right beyond the original input.
- [ ] Test reading an untouched cell as blank.
- [ ] Test replacing an existing symbol without shifting other cells.
- [ ] Test erasing with `WRITE _` without shifting other cells.
- [ ] Test `Reset` after execution.
- [ ] Verify `Step` and `Run` produce the same final configuration for the same program and input.
- [ ] Test nonbinary symbols.
- [ ] Test case-sensitive symbol comparison.
- [ ] Test empty-program rejection.
- [ ] Test unknown-command rejection.
- [ ] Test wrong-argument-count rejection.
- [ ] Test invalid-symbol-token rejection.
- [ ] Test malformed-label rejection, including a label-only line as a separate case.
- [ ] Test duplicate-label rejection.
- [ ] Test undefined-jump-target rejection.
- [ ] Test comment rejection if comments are unsupported.
- [ ] Test rejection of whitespace in the tape input.
- [ ] Test an invalid step limit.
- [ ] Test falling past the end and preservation of the final configuration.
- [ ] Test a loop that reaches `LIMIT REACHED`; explain why the cutoff does not prove nonhalting.
- [ ] Test `HALT` exactly on the last permitted step.

### Additional acceptance check

This check verifies mandatory behavior (`E4`) but is not separately listed in the professor’s required test record.

- [ ] Verify that a runtime error on the last permitted step takes precedence over `LIMIT REACHED`.

### Packaging and supervised assessment

- [ ] Package complete editable source and any required dependency list without passwords, API keys, or unnecessary build folders.
- [ ] Include a README with all required content and offline dependencies.
- [ ] Include sample programs and the completed test record.
- [ ] Include a brief machine-state and execution design explanation.
- [ ] Include the AI-assistance disclosure if applicable.
- [ ] Submit one ZIP by the deadline through the instructor-designated channel.
- [ ] Keep the submitted archive unchanged, make the supervised change in a separate copy, and retain both versions.
- [ ] Complete the unseen trace individually and offline without AI tools, internet, or messaging.
- [ ] Record the approach, implement the assigned modification, run relevant tests, and explain the result in writing.
- [ ] Submit the modified source, tests, and short written explanation from the supervised session.