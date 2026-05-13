package com.canton.etf.iam.sync;

import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.iam.ldap.LdapUser;
import com.canton.etf.iam.ldap.LdapUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LdapSyncService {

    private static final Logger log = LoggerFactory.getLogger(LdapSyncService.class);

    private final LdapUserRepository ldapUserRepository;
    private final LedgerCommandService ledgerCommandService;

    public LdapSyncService(
            LdapUserRepository ldapUserRepository,
            LedgerCommandService ledgerCommandService) {
        this.ldapUserRepository = ldapUserRepository;
        this.ledgerCommandService = ledgerCommandService;
    }

    @Scheduled(fixedDelayString = "${canton.ldap.sync.interval:30000}")
    public void sync() {
        log.info("Starting LDAP sync");

        List<LdapUser> ldapUsers = ldapUserRepository.findAllUsers();

        log.info("Found {} users in LDAP", ldapUsers.size());

        for (LdapUser user : ldapUsers) {
            List<String> roles = ldapUserRepository.findRolesForUser(user.uid());
            LdapUser userWithRoles = new LdapUser(
                    user.dn(), user.uid(), user.displayName(),
                    user.email(), roles);
            syncUser(userWithRoles);
        }

        log.info("LDAP sync complete");
    }

    private void syncUser(LdapUser user) {
        log.info("Syncing user {} with roles {}", user.uid(), user.roles());

        // TODO: query ledger for existing DirectoryEntry
        // If not found: createDirectoryEntry + createRoleMemberships
        // If found and roles changed: updateRoles + sync RoleMemberships
        // If deactivated in LDAP: deactivate DirectoryEntry + revoke RoleMemberships
        // Write AccessEvent for every change
    }
}