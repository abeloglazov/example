package training.mysimpleblog.datamodel;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import training.mysimpleblog.backing.AdminUserBacking;
import training.mysimpleblog.entity.User;

/**
 * Jsf DataModel class for primefaces dataTable. Used in
 * {@link AdminUserBacking}.
 * 
 * @author Aleksejs Beloglazovs
 */
public class UserDataModel extends ListDataModel<User> implements
        SelectableDataModel<User> {

    public UserDataModel() {
    }

    public UserDataModel(List<User> data) {
        super(data);
    }

    @Override
    public User getRowData(String rowKey) {
        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) getWrappedData();
        long userId = Long.valueOf(rowKey);

        for (User user : users) {
            if (user.getId() == userId)
                return user;
        }

        return null;
    }

    @Override
    public Object getRowKey(User user) {
        return user.getId();
    }
}
