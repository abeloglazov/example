package training.mysimpleblog.backing;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;

import training.mysimpleblog.datamodel.PostDataModel;
import training.mysimpleblog.entity.Post;
import training.mysimpleblog.entity.User;
import training.mysimpleblog.service.ContentService;
import training.mysimpleblog.service.UserService;

/**
 * Backing bean class for post management section of admin panel (jsf pages:
 * postList.xhtml, postNew.xhtml, postEdit.xhtml).
 * 
 * @author Aleksejs Beloglazovs
 */
@ManagedBean
@ViewScoped
public class AdminPostBacking implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger
            .getLogger(AdminPostBacking.class.getName());

    private long postId;
    private Post post;
    private Post[] selectedPosts;
    transient private PostDataModel posts;

    @Inject
    private ContentService contentService;

    @Inject
    private UserService userService;

    @Inject
    private AuthBacking authBacking;

    /**************************************
     *** Constructors And Inintializers ***
     **************************************/

    public AdminPostBacking() {
        this.post = new Post();
        logger.info(this.getClass().getSimpleName() + " created");
    }

    @PostConstruct
    private void init() {
        // set up data in table
        this.posts = new PostDataModel(this.contentService.findAllPosts());
        logger.info(this.getClass().getSimpleName() + " initialized");
    }

    /***************************
     *** Getters And Setters ***
     ***************************/

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Post[] getSelectedPosts() {
        return selectedPosts;
    }

    public void setSelectedPosts(Post[] selectedPosts) {
        this.selectedPosts = selectedPosts;
    }

    public PostDataModel getPosts() {
        return posts;
    }

    /**********************
     *** Event Handlers ***
     **********************/

    public void PostAddToViewHandler(ComponentSystemEvent event) {
        this.post = contentService.findPost(this.postId);
    }

    /************************
     *** Business Methods ***
     ************************/

    public String createPost() {
        User currentUser = authBacking.getCurrentUser();
        contentService.createPost(this.post, currentUser);
        return "/admin/post/postList?faces-redirect=true";
    }

    public void updatePost() {
        contentService.updatePost(this.post);
    }

    public String deletePost() {
        contentService.deletePost(post);
        return "/admin/post/postList?faces-redirect=true";
    }

    public void deletePosts(ActionEvent actionEvent) {
        for (Post post : this.selectedPosts) {
            contentService.deletePost(post);
        }

        // refresh data in table
        posts.setWrappedData(this.contentService.findAllPosts());
    }
}
