package com.system.facede.security;

import com.system.facede.model.AdminUser;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class AdminUserDetailsTest {

    @Test
    void shouldReturnCorrectUsernameAndPassword() {
        AdminUser user = new AdminUser();
        user.setUsername("admin");
        user.setPassword("pass");
        user.setRole("ADMIN");

        AdminUserDetails details = new AdminUserDetails(user);

        assertEquals("admin", details.getUsername());
        assertEquals("pass", details.getPassword());
    }

    @Test
    void shouldReturnGrantedAuthorityWithCorrectRole() {
        AdminUser user = new AdminUser();
        user.setRole("admin");  // lowercase to test .toUpperCase()

        AdminUserDetails details = new AdminUserDetails(user);

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();
        assertEquals(1, authorities.size());

        String role = authorities.iterator().next().getAuthority();
        assertEquals("ROLE_ADMIN", role);
    }

    @Test
    void shouldReturnAdminUserReference() {
        AdminUser user = new AdminUser();
        AdminUserDetails details = new AdminUserDetails(user);

        assertSame(user, details.getAdminUser());
    }

    @Test
    void allAccountFlagsShouldBeTrue() {
        AdminUser user = new AdminUser();
        AdminUserDetails details = new AdminUserDetails(user);

        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
    }
}
