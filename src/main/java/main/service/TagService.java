package main.service;

import main.dto.TagsDTO;
import main.dto.api.response.TagResponse;
import main.model.Tag;
import main.model.repositories.PostRepository;
import main.model.repositories.Tag2PostRepository;
import main.model.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.*;

//@Service
//public class TagService {
//    private final TagRepository tagRepository;
//
//
//    public TagService(TagRepository tagRepository) {
//        this.tagRepository = tagRepository;
//    }
//
//    public TagsResponse getTags(String query) {
//
//        List<Tag> tagList = tagRepository.findAll();
//        TagsResponse tagsResponse = new TagsResponse();
//        List<TagsDTO> finishTagList = new ArrayList<>();
//
//        if (tagList.size() > 0) {
//            Tag popularTag = tagList.stream().max(Comparator.comparing(t -> t.getPostWithTags().size())).get();
//            int tagsCount = tagList.size();
//            System.out.println(popularTag.getName());
//            System.out.println(popularTag.getPostWithTags().size());
//            System.out.println(tagsCount);
//            double k = 1.0 / (popularTag.getPostWithTags().size() / tagsCount);
//            if (query == null) {
//                tagList.forEach(t -> {
//                    TagsDTO tagDto = new TagsDTO();
//                    tagDto.setName(t.getName());
//                    tagDto.setWeight(t.getPostWithTags().size() / tagsCount * k);
//                    finishTagList.add(tagDto);
//                });
//            } else {
//                tagList.forEach(t -> {
//                    if (t.getName().equals(query)) {
//                        TagsDTO tagDto = new TagsDTO();
//                        tagDto.setName(t.getName());
//                        tagDto.setWeight((double) Math.round(t.getPostWithTags().size() / tagsCount * k));
//                        finishTagList.add(tagDto);
//                    }
//                });
//            }
//            tagsResponse.setTags(finishTagList);
//        }
//        return tagsResponse;
//    }
//}




@Service
public class TagService {
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final Tag2PostRepository tag2PostRepository;

    public TagService(TagRepository tagRepository, PostRepository postRepository, Tag2PostRepository tag2PostRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.tag2PostRepository = tag2PostRepository;
    }

    public TagResponse getTags() {
        TagResponse tagResponse = new TagResponse();

        List<TagsDTO> tagsDTOList = toDTOTags();
        tagResponse.setTags(tagsDTOList);
        return tagResponse;
    }

    private List<TagsDTO> toDTOTags() {
        List<Tag> allTags = tagRepository.findAll();
        int countAllPosts = postRepository.countPosts();
        int weights = allTags.size();

        int maxTag = allTags.stream().mapToInt(v ->
                tag2PostRepository.countOfPostsWithTheName(v.getName())).max().getAsInt();

        List<TagsDTO> tagsDTOList = new ArrayList<>();

        for (int a = 0; a < allTags.size(); a++) {
            TagsDTO tagsDTO = new TagsDTO();
            String tagsName = allTags.get(a).getName();

            int countOfTagsByName = tag2PostRepository.countOfPostsWithTheName(tagsName); //Количество тегов с конкретным именем в постах
            double dWeightHibernate = Double.valueOf(countOfTagsByName) / Double.valueOf(countAllPosts); // dWeightHibernate = hibernate / weights = 4 / 20 = 0.20
            double dWeightMax = Double.valueOf(maxTag) / Double.valueOf(weights);  //dWeightMax = java / weights = 18 / 20 = 0.90
            double k = 1.0 / dWeightMax; //k = 1 / dWeightMax = 1 / 0.90 = 1.11
            double weightTag = dWeightHibernate * k; // weightHibernate = hibernate * k = 0.20 * 1.11 = 0.22

            tagsDTO.setName(tagsName);
            tagsDTO.setWeight(weightTag);
            tagsDTOList.add(tagsDTO);


        }


        return tagsDTOList;
    }


}
