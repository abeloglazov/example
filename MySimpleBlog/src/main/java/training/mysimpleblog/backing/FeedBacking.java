package training.mysimpleblog.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import training.mysimpleblog.entity.Post;
import training.mysimpleblog.service.ContentService;

/**
 * Backing bean class for feed.xhtml jsf page.
 * 
 * @author Aleksejs Beloglazovs
 */
@Named
@RequestScoped
public class FeedBacking implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(FeedBacking.class
            .getName());

    @Inject
    private ContentService contentService;

    private String tag;
    private int page;
    private int pageSize;

    /**************************************
     *** Constructors And Inintializers ***
     **************************************/

    public FeedBacking() {
        logger.info(this.getClass().getSimpleName() + " created");
    }

    @PostConstruct
    private void init() {
        int pageSize = Integer.valueOf(contentService.findSettingByName(
                "feedPageSize").getValue());
        this.setPageSize(pageSize);
        this.setPage(1);
        logger.info(this.getClass().getSimpleName() + " initialized");
    }

    /***************************
     *** Getters And Setters ***
     ***************************/

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**********************
     *** Event Handlers ***
     **********************/

    public void PreRenderViewHandler(ComponentSystemEvent event) {
        if (this.page < 1) {
            this.page = 1;
        } else if (this.page > this.getLastPage()) {
            this.page = this.getLastPage();
        }
    }

    /************************
     *** Business Methods ***
     ************************/

    public List<Post> getPosts() {
        if (this.tag != null) {
            return contentService.findPostsByTagAndPage(tag, page, pageSize);
        } else {
            return contentService.findPostsByPage(page, pageSize);
        }
    }

    public int getLastPage() {
        double postCount;

        if (this.tag != null) {
            postCount = contentService.countPostsByTag(this.tag);
        } else {
            postCount = this.contentService.countAllPosts();
        }

        int result = (int) Math.ceil(postCount / pageSize);

        return result < 1 ? 1 : result;
    }

    public boolean hasPreviousPage() {
        return this.page < this.getLastPage();
    }

    public boolean hasNextPage() {
        return this.page > 1;
    }

    public int getPreviousPage() {
        if (hasPreviousPage()) {
            return page + 1;
        } else {
            return 1;
        }
    }

    public int getNextPage() {
        if (hasNextPage()) {
            return page - 1;
        } else {
            return 1;
        }
    }

    public List<String[]> getWeightedTags() {
        List<Object[]> countedTags = contentService.findCountedTags();
        List<String[]> result = new ArrayList<String[]>();

        double maximum = 1.0;
        double minimum = Double.MAX_VALUE;

        // get maximum and minimum tag occurrence
        for (Object[] objects : countedTags) {
            double tagCount = ((Long) objects[1]).doubleValue();
            if (tagCount > maximum) {
                maximum = tagCount;
            }
            if (tagCount < minimum) {
                minimum = tagCount;
            }
        }

        // find distribution, to determine tag ranges
        double distribution = (maximum - minimum) / 5;

        // convert to String[] and calculate tag weight
        for (Object[] objects : countedTags) {

            String[] weightedTag = new String[3];
            weightedTag[0] = (String) objects[0];
            weightedTag[1] = ((Long) objects[1]).toString();

            double tagCount = ((Long) objects[1]).doubleValue();

            // first interval
            if (tagCount >= minimum && tagCount < (minimum + distribution)) {
                weightedTag[2] = "smallest-tag";
                // second interval
            } else if (tagCount >= (minimum + distribution)
                    && tagCount < (minimum + (distribution * 2.0))) {
                weightedTag[2] = "small-tag";
                // third interval
            } else if (tagCount >= (minimum + (distribution * 2.0))
                    && tagCount < (minimum + (distribution * 3.0))) {
                weightedTag[2] = "medium-tag";
                // fouth interval
            } else if (tagCount >= (minimum + (distribution * 3.0))
                    && tagCount < (minimum + (distribution * 4.0))) {
                weightedTag[2] = "large-tag";
                // fifth interval
            } else if (tagCount >= (minimum + (distribution * 4.0))
                    && tagCount <= maximum) {
                weightedTag[2] = "largest-tag";
            }

            result.add(weightedTag);
        }

        return result;
    }
}
