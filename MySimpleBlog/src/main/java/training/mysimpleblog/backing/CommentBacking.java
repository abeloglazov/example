package training.mysimpleblog.backing;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;

import training.mysimpleblog.entity.Comment;
import training.mysimpleblog.service.ContentService;

/**
 * Backing bean class for comment.xhtml jsf page. Handles comment update.
 * 
 * @author Aleksejs Beloglazovs
 */
@ManagedBean
@ViewScoped
public class CommentBacking implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(PostBacking.class
            .getName());

    private long commentId;
    private Comment comment;

    @Inject
    private ContentService contentService;

    @Inject
    private AuthBacking authBacking;

    /********************
     *** Constructors ***
     ********************/

    public CommentBacking() {
        logger.info(this.getClass().getSimpleName() + " created");
    }

    /***************************
     *** Getters And Setters ***
     ***************************/

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    /**********************
     *** Event Handlers ***
     **********************/

    public void PostAddToViewHandler(ComponentSystemEvent event)
            throws IOException {
        this.comment = contentService.findComment(this.commentId);

        FacesContext context = FacesContext.getCurrentInstance();

        if (!context.isPostback() && !canEdit()) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("../../");
        }
    }

    /************************
     *** Business Methods ***
     ************************/

    public String updateComment() throws IOException {
        if (canEdit()) {
            long postId = comment.getPost().getId();
            contentService.updateComment(this.comment);

            return "post?faces-redirect=true&&postId=" + String.valueOf(postId);
        } else {
            return "feed?faces-redirect=true";
        }
    }

    private boolean canEdit() {
        return authBacking.isLoggedIn()
                && (authBacking.isCurrentStaff() || authBacking
                        .isCurrentCommentAuthor(this.comment));
    }
}