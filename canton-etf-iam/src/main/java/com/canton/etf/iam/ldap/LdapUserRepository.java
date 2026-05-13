package com.canton.etf.iam.ldap;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LdapUserRepository {

    private final LdapTemplate ldapTemplate;

    public LdapUserRepository(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public List<LdapUser> findAllUsers() {
        return ldapTemplate.search(
                "ou=people",
                "(objectClass=person)",
                new AbstractContextMapper<LdapUser>() {
                    @Override
                    protected LdapUser doMapFromContext(
                            org.springframework.ldap.core.DirContextOperations ctx) {
                        String dn = ctx.getDn().toString();
                        String uid = ctx.getStringAttribute("uid");
                        String displayName = ctx.getStringAttribute("displayName");
                        String email = ctx.getStringAttribute("mail");
                        return new LdapUser(dn, uid, displayName, email, new ArrayList<>());
                    }
                });
    }

    public List<String> findRolesForUser(String uid) {
        return ldapTemplate.search(
                "ou=groups",
                "(memberUid=" + uid + ")",
                new AbstractContextMapper<String>() {
                    @Override
                    protected String doMapFromContext(
                            org.springframework.ldap.core.DirContextOperations ctx) {
                        return mapGroupToCanton(ctx.getStringAttribute("cn"));
                    }
                });
    }

    private String mapGroupToCanton(String groupCn) {
        return switch (groupCn) {
            case "FundManagers" -> "FundManager";
            case "Custodians" -> "Custodian";
            case "ComplianceOfficers" -> "ComplianceOfficer";
            case "Auditors" -> "Auditor";
            case "MarketMakers" -> "MarketMaker";
            default -> null;
        };
    }
}