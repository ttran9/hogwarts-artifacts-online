package edu.tcu.cs.hogwartsartifactsonline.hogwartsuser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

//@Component
public class MyUserPrincipal implements UserDetails {

    /**
     * Wrap the user instance returned by the UserRepository
     */
    private final HogwartsUser hogwartsUser;

    public MyUserPrincipal(HogwartsUser user) {
        this.hogwartsUser = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /*
         * The HogwartsUser has a list of roles separated by a string.
         * We would need to split each role by using a delimiter.
         * We convert a user's roles to a list of SimpleGrantedAuthority objects.
         * John's roles are stored as "admin user" so we need to convert it into a list.
         * We need to add a "ROLE_" prefix before each role name.
         */
//        StringUtils.tokenizeToStringArray(this.hogwartsUser.getRoles(), " ").
        return Arrays.stream(StringUtils.tokenizeToStringArray(this.hogwartsUser.getRoles(), " "))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }

    @Override
    public String getPassword() {
        return this.hogwartsUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.hogwartsUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.hogwartsUser.isEnabled();
    }

    public HogwartsUser getHogwartsUser() {
        return hogwartsUser;
    }
}
