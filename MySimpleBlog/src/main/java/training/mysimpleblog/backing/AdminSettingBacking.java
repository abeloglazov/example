package training.mysimpleblog.backing;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import training.mysimpleblog.entity.Setting;
import training.mysimpleblog.service.ContentService;

/**
 * Backing bean class for settings management section of admin panel (jsg page
 * settingList.xhtml).
 * 
 * @author Aleksejs Beloglazovs
 */
@Named
@RequestScoped
public class AdminSettingBacking implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger
            .getLogger(AdminUserBacking.class.getName());

    private List<Setting> settings;

    @Inject
    private ContentService contentService;

    /**************************************
     *** Constructors And Inintializers ***
     **************************************/

    public AdminSettingBacking() {
        logger.info(this.getClass().getSimpleName() + " created");
    }

    @PostConstruct
    private void init() {
        this.setSettings(contentService.findAllSettings());
        logger.info(this.getClass().getSimpleName() + " initialized");
    }

    /***************************
     *** Getters And Setters ***
     ***************************/

    public List<Setting> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }

    /************************
     *** Business Methods ***
     ************************/

    public void updateSettings(ActionEvent actionEvent) {
        contentService.updateSettings(this.getSettings());
    }
}
