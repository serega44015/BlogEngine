package main.service;

import main.dto.api.response.TagsResponse;
import main.dto.TagsDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagsService {

    public TagsResponse getTags(String tagName){
        TagsResponse tagsResponse = new TagsResponse();
        TagsDTO tagsDTO = new TagsDTO();
        List<TagsDTO> tagsDTOList = new ArrayList<>();
        tagsDTO.setName("firstTag");
        tagsDTO.setWeight(8.8);

        tagsDTOList.add(tagsDTO);
        tagsResponse.setTagsDTO(tagsDTOList);
        return tagsResponse;
    }

}
