package training.mysimpleblog.datamodel;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import training.mysimpleblog.backing.AdminPostBacking;
import training.mysimpleblog.entity.Post;

/**
 * Jsf DataModel class for primefaces dataTable. Used in
 * {@link AdminPostBacking}.
 * 
 * @author Aleksejs Beloglazovs
 */
public class PostDataModel extends ListDataModel<Post> implements
        SelectableDataModel<Post> {

    public PostDataModel() {
    }

    public PostDataModel(List<Post> data) {
        super(data);
    }

    @Override
    public Post getRowData(String rowKey) {
        @SuppressWarnings("unchecked")
        List<Post> posts = (List<Post>) getWrappedData();
        long postId = Long.valueOf(rowKey);

        for (Post post : posts) {
            if (post.getId() == postId)
                return post;
        }

        return null;
    }

    @Override
    public Object getRowKey(Post post) {
        return post.getId();
    }
}
