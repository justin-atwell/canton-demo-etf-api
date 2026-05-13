package com.canton.etf.iam.ldap;

import java.util.List;

public record LdapUser(
        String dn,
        String uid,
        String displayName,
        String email,
        List<String> roles
) {}