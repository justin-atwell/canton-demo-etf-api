package com.canton.etf.iam.sync;

import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.iam.ldap.LdapUser;
import com.canton.etf.iam.ldap.LdapUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LdapSyncServiceTest {

    @Mock
    private LdapUserRepository ldapUserRepository;

    @Mock
    private LedgerCommandService ledgerCommandService;

    @InjectMocks
    private LdapSyncService testObject;

    @Test
    void sync_callsLdapRepository() {
        testObject.sync();

        Mockito.verify(ldapUserRepository).findAllUsers();
    }

    @Test
    void sync_withUser_callsFindRolesForEachUser() {
        var testUsers = new ArrayList<LdapUser>();
        var roles = new ArrayList<String>();
        roles.add("FundManager");
        testUsers.add(new LdapUser("myDn", "11111cd", "Kevin bacon", "Kevin@bacon.com", roles));

        when(ldapUserRepository.findAllUsers()).thenReturn(testUsers);

        testObject.sync();
    }

    @Test
    void sync_noUsers_doesntcallfindroles() {
        when(ldapUserRepository.findAllUsers()).thenReturn(List.of());

        testObject.sync();

        Mockito.verify(ldapUserRepository, Mockito.never()).findRolesForUser(any());

    }
}
