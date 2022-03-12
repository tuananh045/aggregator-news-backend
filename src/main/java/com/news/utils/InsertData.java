package com.news.utils;

import com.news.model.ERole;
import com.news.model.Role;
import com.news.model.Source;
import com.news.repository.RoleRepository;
import com.news.repository.SourceRepository;
import com.news.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InsertData implements ApplicationListener<ContextRefreshedEvent>, InitializingBean {

    private static boolean eventFired = false;
    private static final Logger logger = LoggerFactory.getLogger(InsertData.class);

    @Autowired
    private UserRepository repos;

    @Autowired
    private RoleRepository roleRepos;

    @Autowired
    private SourceRepository sourceRepos;

    @Autowired
    private PasswordEncoder encoder;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (eventFired) {
            return;
        }
        logger.info("Application started.");

        eventFired = true;

        try {
//            createRoles();
//            createAdminUser();
            createSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createRoles() {
        List<Role> roleNames = new ArrayList<>();
        roleNames.add(new Role(ERole.ROLE_ADMIN));
        roleNames.add(new Role(ERole.ROLE_USER));

        for (Role roleName : roleNames) {
            if (roleRepos.existsByName(roleName.getName())) {
                return;
            }
            roleName.setName(roleName.getName());
            try {
                roleRepos.save(roleName);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    private void createSource() {
        if (sourceRepos.existsBySlug("vietnamnet") || sourceRepos.existsBySlug("tuoi-tre")
                || sourceRepos.existsBySlug("thanh-nien") || sourceRepos.existsBySlug("vnexpress")) {
            return;
        } else {
            List<Source> entities = new ArrayList<Source>();
            Source vnexpress = new Source("VnExpress", "vnexpress", "vne_logo_rss.png");
            Source vietnamnet = new Source("VietNamNet", "vietnamnet", "Vietnamnet-Logo.png");
            Source tt = new Source("Tuổi Trẻ Online", Slug.makeCode("Tuổi Trẻ"), "Tuổi_Trẻ_Logo.png");
            Source tn = new Source("Thanh Niên Online", Slug.makeCode("Thanh Niên"), "Thanh_Niên_logo.png");
            entities.add(vietnamnet);
            entities.add(tt);
            entities.add(tn);
            entities.add(vnexpress);
            try {
                sourceRepos.saveAll(entities);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }
}
