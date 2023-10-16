package com.accionmfb.proxy.mobileproxyv2.repository;

import com.accionmfb.proxy.mobileproxyv2.model.AppUser;
import com.accionmfb.proxy.mobileproxyv2.model.RoleGroups;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class MobileProxyRepositoryImpl implements MobileProxyRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    public AppUser getAppUserUsingChannel(String channel){
        return em.createQuery("select a from AppUser a where a.channel = :channel", AppUser.class)
                .setParameter("channel", channel)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public AppUser getAppUserUsingUsername(String username){
        return em.createQuery("select a from AppUser a where a.username = :username", AppUser.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getEncryptionKey(String username) {
        TypedQuery<String> query = em.createQuery("SELECT t.encryptionKey FROM AppUser t WHERE t.username = :username", String.class)
                .setParameter("username", username);
        List<String> record = query.getResultList();
        if (record.isEmpty()) {
            return null;
        }
        return record.get(0);
    }

    @Override
    public String getUserAllowedApps(String username) {
        AppUser appUser = this.getAppUserUsingUsername(username);
        if (appUser != null) {
            RoleGroups roleGroup = this.getRoleGroupUsingGroupName(appUser.getRole().getGroupName());
            if (roleGroup == null) {
                return "";
            }
            List<String> userRoles = this.getUserRoles(roleGroup);
            return userRoles == null ? "" : userRoles.toString();
        }
        return "";
    }

    @Override
    public RoleGroups getRoleGroupUsingGroupName(String groupName) {
        TypedQuery<RoleGroups> query = em.createQuery("SELECT t FROM RoleGroups t WHERE t.groupName = :groupName", RoleGroups.class)
                .setParameter("groupName", groupName);
        List<RoleGroups> record = query.getResultList();
        if (record.isEmpty()) {
            return null;
        }
        return record.get(0);
    }

    @Override
    public List<String> getUserRoles(RoleGroups roleGroup) {
        TypedQuery<String> query = em.createQuery("SELECT t.appRole.roleName FROM GroupRoles t WHERE t.roleGroup = :roleGroup", String.class)
                .setParameter("roleGroup", roleGroup);
        List<String> record = query.getResultList();
        if (record.isEmpty()) {
            return null;
        }
        return record;
    }
}
