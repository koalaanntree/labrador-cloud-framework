package net.bestjoy.cloud.security.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/***
 * security user
 * @author ray
 */
@Data
@ToString
@NoArgsConstructor
public class AppUserDetails implements UserDetails {

    /**
     * 用户昵称
     */
    private String username;
    /**
     * 账户密码
     */
    private String password;

    /***
     * 昵称
     */
    private String nickname;

    /***
     * 头像地址
     */
    private String avatar;

    /**
     * 签名
     */
    private String signature;

    /***
     * 用户ID
     */
    private String userId;

    /***
     * 创建时间
     */
    private Date createTime;


    public AppUserDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Setter
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }
}
