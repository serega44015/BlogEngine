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
        Integer countAllPosts = postRepository.countPosts();
        //Optional<Tags2Post> test = tag2PostRepository.findByName();
        //Получить список тегов и прогнать через базу данных каждое, получая количество постов, в которых они содержатся, можно через For
        Integer weights = allTags.size();

        String nameMaxTag = null; //имя топ тега
        int maxTag = 0;//количество тегов, которое есть у постов
        for (int b = 0; b < allTags.size(); b++){
            //Находим самый популярный тег, лучше потом сделать через stream и искать max значение
            String tagName = allTags.get(b).getName();
            int countValue = tag2PostRepository.countOfPostsWithTheName(tagName);//количество тегов в постах
            if (countValue > maxTag){
                maxTag = countValue;
                nameMaxTag = tagName;
            }
        }
        System.out.println("Имя топ тега " + nameMaxTag + " Количество: " + maxTag);


        List<TagsDTO> tagsDTOList = new ArrayList<>();
        TagsDTO tagsDTO = new TagsDTO();

        for (int a = 0; a < allTags.size(); a++){

            String tagsName = allTags.get(a).getName();
            System.out.println("Кол-во: " + tag2PostRepository.countOfPostsWithTheName(tagsName) + " с именем - " + tagsName + " А ЕСЛИ САЙЗ ЛИСТА " + allTags.size());

            int countOfTagsByName = tag2PostRepository.countOfPostsWithTheName(tagsName); //Количество тегов с конкретным именем в постах
            double dWeightHibernate = Double.valueOf(countOfTagsByName) / Double.valueOf(countAllPosts); // dWeightHibernate = hibernate / weights = 4 / 20 = 0.20
            double dWeightMax = Double.valueOf(maxTag) / Double.valueOf(weights);  //dWeightMax = java / weights = 18 / 20 = 0.90
            double k = 1.0 / dWeightMax; //k = 1 / dWeightMax = 1 / 0.90 = 1.11
            double weightTag = dWeightHibernate * k; // weightHibernate = hibernate * k = 0.20 * 1.11 = 0.22

            if (a == 9){
                System.out.println("sss");
            }

            //крч найти, почему все теги php
            tagsDTO.setName(tagsName);
            tagsDTO.setWeight(weightTag);
            tagsDTOList.add(tagsDTO);


        }

        return tagsDTOList;
    }

}
