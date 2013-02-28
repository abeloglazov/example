package training.mysimpleblog.backing;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;

import training.mysimpleblog.entity.Comment;
import training.mysimpleblog.entity.Post;
import training.mysimpleblog.entity.User;
import training.mysimpleblog.service.ContentService;
import training.mysimpleblog.service.UserService;

/**
 * Backing bean class for post.xhtml jsf page.
 * 
 * @author Aleksejs Beloglazovs
 */
@ManagedBean
@ViewScoped
public class PostBacking implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(PostBacking.class
            .getName());

    private long postId;
    private int page;
    private int pageSize;
    private Post post;
    private Comment newComment;
    private Comment selectedComment;

    @Inject
    private ContentService contentService;

    @Inject
    private UserService userService;

    @Inject
    private AuthBacking authBacking;

    /**************************************
     *** Constructors And Inintializers ***
     **************************************/

    public PostBacking() {
        this.post = new Post();
        this.newComment = new Comment();
        logger.info(this.getClass().getSimpleName() + " created");
    }

    @PostConstruct
    private void init() {
        int pageSize = Integer.valueOf(contentService.findSettingByName(
                "discussionPageSize").getValue());
        this.setPageSize(pageSize);
        this.setPage(1);
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getNewComment() {
        return newComment;
    }

    public void setNewComment(Comment newComment) {
        this.newComment = newComment;
    }

    public Comment getSelectedComment() {
        return selectedComment;
    }

    public void setSelectedComment(Comment selectedComment) {
        this.selectedComment = selectedComment;
    }

    /**********************
     *** Event Handlers ***
     **********************/

    public void PostAddToViewHandler(ComponentSystemEvent event) {
        this.post = contentService.findPost(this.postId);
    }

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

    public void createComment() throws IOException {
        User currentUser = authBacking.getCurrentUser();
        contentService.createComment(this.newComment, this.post, currentUser);
        this.newComment = new Comment();

        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("?page=" + this.page + "#discussion");
    }

    public void deleteComment() {
        contentService.deleteComment(selectedComment);
    }

    public List<Comment> getCommentsByPage() {
        int commentsCount = this.post.getComments().size();
        int from = (this.page - 1) * this.pageSize;
        int to = (this.page * this.pageSize) > commentsCount ? commentsCount
                : this.page * this.pageSize;

        return this.post.getComments().subList(from, to);
    }

    public int getLastPage() {
        double commentsCount = this.post.getComments().size();
        int result = (int) Math.ceil(commentsCount / pageSize);

        return result < 1 ? 1 : result;
    }

    public List<Integer> getPages() {
        List<Integer> list = new ArrayList<Integer>();

        for (int i = 1; i <= this.getLastPage(); i++) {
            list.add(i);
        }

        return list;
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

    public boolean hasComments() {
        return !post.getComments().isEmpty();
    }
}