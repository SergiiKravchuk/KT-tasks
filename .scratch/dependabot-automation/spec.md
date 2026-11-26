# Dependabot configuration

Status: ready-for-agent

Design confirmed by the user; Q1-Q6 are settled.

Implementation: completed locally on 2026-11-26.

## Scope

Configure Dependabot for this repository. Workflows, review skills, auto-merge implementation, and dependency-review configuration are outside scope. Retain the draft GitHub Actions update entry without creating workflows. Repository security settings are enabled as reported by the user.

## Decisions

- Preserve the draft Spring, Jackson, Netty, testing, and remaining groups and patterns. Artifact families are review collections; coordinated release cycles are not required.
- Security updates use five family groups with the suffix -security and no update-level filter. Major security fixes remain grouped and require migration review when breaking changes are involved.
- Ordinary updates use ten family/update-level groups: patches and minors separately. Ordinary major updates remain individual PRs for manual migration review.
- Put each catch-all after the named families for its flow and update level. First matching group wins.
- Preserve Maven parent/BOM management. Classify the coordinate being updated, without introducing overrides solely to split inherited dependencies into family groups.
- For example, a routine Spring Boot parent patch belongs to spring-patches, even if inherited Jackson or testing libraries also change. The patch designation describes the parent version change, not every transitive change and not a safety guarantee.
- Security updates mean alert-driven Dependabot fixes. Ordinary version updates may also remediate vulnerabilities; group names do not establish that every alert is resolved.
- Maven version checks remain weekly, Monday at 07:00 Europe/Kyiv, with up to 10 open version PRs. GitHub Actions checks remain Monday at 07:30. Security remediation is not restricted to that weekly version-check schedule and has a separate fixed limit of 10 open PRs.
- File-level security groups take precedence over repository/organization grouping settings for configured directories; an additional repository-wide grouping switch is not required for this coverage.

## Coverage boundary

The current POM declares a Spring Boot parent and inherits starter and plugin versions. These are not independent local version pins. Grouping cannot force Maven transitive remediation that Dependabot does not support; some alerts may require manually updating the parent/BOM or making a deliberate override. This configuration does not guarantee a PR for every alert.

## Applied artifact

The approved [proposal](./dependabot.proposed.yml) has been copied unchanged to [.github/dependabot.yml](../../.github/dependabot.yml).

## Review checks

- Five security groups, ten ordinary update groups, and two ecosystem entries.
- Named groups precede catch-alls; security groups have no update-level filter.
- Ordinary groups cover patches and minors only, with no major ignore rule.
- Original namespace patterns and schedules are preserved.
- YAML parsed successfully with the locally installed js-yaml parser. Structural checks and 42 local grouping scenarios passed, covering each named family, all testing namespaces, catch-all routing, and patch/minor/major behavior in both flows. The applied file matches the approved proposal exactly.
- GitHub-hosted Dependabot execution remains unverified; local checks validate configuration structure and expected rule matching, not the hosted updater.

## Sources

- [GitHub options reference](https://docs.github.com/en/code-security/reference/supply-chain-security/dependabot-options-reference)
- [GitHub security update configuration](https://docs.github.com/en/code-security/how-tos/secure-your-supply-chain/secure-your-dependencies/configure-security-updates)
- [GitHub security update coverage](https://docs.github.com/en/code-security/concepts/supply-chain-security/dependabot-security-updates)
- [Spring Boot parent dependency management](https://docs.spring.io/spring-boot/3.3/maven-plugin/using.html)

## Comments

- Round 1: accepted repository-specific scope and individual ordinary major PRs; retained original review groups regardless of release coordination; clarified alert-driven security terminology.
- Round 2: accepted preserving parent/BOM ownership and grouping major security fixes.
- Final confirmation: user accepted the complete design; applied the approved proposal unchanged and completed local validation.
