package main.service;

import main.dto.api.response.TagsResponse;
import main.dto.TagsDTO;
import main.model.Tag;
import main.model.Tags2Post;
import main.model.repositories.PostRepository;
import main.model.repositories.Tag2PostRepository;
import main.model.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagsService {
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final Tag2PostRepository tag2PostRepository;
    public TagsService(TagRepository tagRepository, PostRepository postRepository, Tag2PostRepository tag2PostRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.tag2PostRepository = tag2PostRepository;
    }

    public TagsResponse getTags(String tagName){
        TagsResponse tagsResponse = new TagsResponse();

        List<TagsDTO> tagsDTOList = toDTOTags();
        tagsResponse.setTagsDTO(tagsDTOList);
        return tagsResponse;
    }

    private List<TagsDTO> toDTOTags(){

        List<Tag> allTags = tagRepository.findAll();
        Integer countPosts = postRepository.countPosts();
        //Optional<Tags2Post> test = tag2PostRepository.findByName();
        //Получить список тегов и прогнать через базу данных каждое, получая количество постов, в которых они содержатся, можно через For
        String tagName = "Java";
        Integer test = tag2PostRepository.countOfPostsWithTheName(tagName);
        System.out.println("А МЫ ВООБЩЕ СЮДА ПОПАДАЕМ?" + " " + test);
        List<TagsDTO> tagsDTOList = new ArrayList<>();
        TagsDTO tagsDTO = new TagsDTO();

        tagsDTO.setName("firstTag");
        tagsDTO.setWeight(8.8);
        tagsDTOList.add(tagsDTO);

        return tagsDTOList;
    }

}
