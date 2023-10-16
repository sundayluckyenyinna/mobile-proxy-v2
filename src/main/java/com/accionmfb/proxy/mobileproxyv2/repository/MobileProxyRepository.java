package com.accionmfb.proxy.mobileproxyv2.repository;

import com.accionmfb.proxy.mobileproxyv2.model.AppUser;
import com.accionmfb.proxy.mobileproxyv2.model.RoleGroups;

import java.util.List;

public interface MobileProxyRepository {
    AppUser getAppUserUsingChannel(String channel);

    AppUser getAppUserUsingUsername(String username);

    String getEncryptionKey(String username);

    String getUserAllowedApps(String username);

    RoleGroups getRoleGroupUsingGroupName(String groupName);

    List<String> getUserRoles(RoleGroups roleGroup);
}
