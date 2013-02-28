package training.mysimpleblog.converter;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import training.mysimpleblog.entity.Tag;
import training.mysimpleblog.service.ContentService;

/**
 * Jsf converter class for {@link Tag}
 * 
 * @author Aleksejs Beloglazovs
 */
@Named
@ApplicationScoped
public class TagsConverter implements Converter {

    @Inject
    private ContentService contentService;

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        @SuppressWarnings("unchecked")
        List<Tag> tags = (List<Tag>) value;

        StringBuilder result = new StringBuilder();

        for (Tag tag : tags) {
            result.append(tag.getName());

            // if not last, then add ","
            int lastIndex = tags.size() - 1;
            if (tags.indexOf(tag) != lastIndex) {
                result.append(", ");
            }
        }

        return result.toString();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value == null || value.equals("")) {
            return null;
        }

        List<Tag> result = new ArrayList<Tag>();
        String[] sTags = value.split(",");

        for (String sTag : sTags) {
            sTag = sTag.trim();
            Tag tag = contentService.findTagByName(sTag);

            // if new tag
            if (tag == null) {
                tag = new Tag();
                tag.setName(sTag);
            }

            result.add(tag);
        }

        return result;
    }

}
