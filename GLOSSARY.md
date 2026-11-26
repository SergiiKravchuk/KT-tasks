# Dependency Maintenance

Dependency updates are organized for review and vulnerability remediation in this repository.

## Language

**Artifact family**:
A named collection of dependencies reviewed together for convenience. Its members need not share a release cycle or version number.

**Security update**:
An alert-driven dependency update intended to remediate a known vulnerability. It need not resolve every vulnerability in the application.

**Version update**:
A routine dependency update to a newer release, which may also remediate vulnerabilities.
_Avoid_: Normal update

**Migration update**:
An ordinary major-version update requiring explicit migration review.
