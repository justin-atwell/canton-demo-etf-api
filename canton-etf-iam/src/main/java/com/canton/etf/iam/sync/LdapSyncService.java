package com.canton.etf.iam.sync;

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

    public LdapSyncService(LdapUserRepository ldapUserRepository) {
        this.ldapUserRepository = ldapUserRepository;
    }

    @Scheduled(fixedDelayString = "${canton.ldap.sync.interval:30000}")
    public void sync() {
        log.info("Starting LDAP sync");

        List<LdapUser> ldapUsers = ldapUserRepository.findAllUsers();

        log.info("Found {} users in LDAP", ldapUsers.size());

        for (LdapUser user : ldapUsers) {
            List<String> roles = ldapUserRepository.findRolesForUser(user.uid());
            LdapUser userWithRoles = new LdapUser(
                    user.dn(),
                    user.uid(),
                    user.displayName(),
                    user.email(),
                    roles
            );
            syncUser(userWithRoles);
        }

        log.info("LDAP sync complete");
    }

    private void syncUser(LdapUser user) {
        // TODO: diff against ledger DirectoryEntry contracts
        // For now just log
        log.info("Syncing user {} with roles {}", user.uid(), user.roles());
    }
}