# Dependabot automation design

## Notes

- Scope: Dependabot configuration for Maven version updates and security updates. Workflows and review skills are excluded by the user.
- Repository observation: root pom.xml inherits spring-boot-starter-parent 3.3.2; two starters and the Boot plugin omit versions. No existing .github configuration was present in the root inventory.
- Spring Boot parent supplies dependency management: https://docs.spring.io/spring-boot/3.3/maven-plugin/using.html
- GitHub grouping and update classification reference: https://docs.github.com/en/code-security/reference/supply-chain-security/dependabot-options-reference
- Configuration-file security groups take precedence over repository and organization settings: https://docs.github.com/en/code-security/reference/supply-chain-security/dependabot-security-updates

## Decisions-so-far

- Workflows and review skills are outside scope.
- Repository security settings are enabled, as reported by the user.
- Q1: target this repository only.
- Q2: retain the draft's Spring, Jackson, Netty, testing, and remaining review groups. Coordinated release/version ownership is not a prerequisite for membership. Ordinary updates retain the draft's patch/minor separation.
- Q3: ordinary major updates remain individual migration PRs for manual review.
- Q4: security groups contain alert-driven Dependabot security updates intended to remediate vulnerabilities. Ordinary version updates may also fix vulnerabilities; security grouping does not imply all alerts are resolved.

- Q5: preserve parent/BOM ownership; group the updated coordinate without adding overrides solely for grouping.
- Q6: major security fixes remain in security groups.

## Fog

- No remaining design questions. User confirmed the complete design.
- Approved configuration applied to .github/dependabot.yml. YAML parsing, structural checks, and 42 local grouping scenarios passed. Hosted execution remains unverified.

## Comments

- Round 1: user accepted repository-specific scope, individual ordinary major PRs, and alert-driven security terminology; rejected coordinated-release grouping as a requirement and retained the draft review groups.
- Round 2: user accepted Q5 and Q6. Prepared concrete specification and proposed YAML for final confirmation.
- Final confirmation: user accepted shared understanding; implementation completed locally.
