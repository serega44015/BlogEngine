package main.service;

import main.dto.TagDto;
import main.dto.api.response.TagResponse;
import main.model.Tag;
import main.model.repositories.PostRepository;
import main.model.repositories.Tag2PostRepository;
import main.model.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
  private final TagRepository tagRepository;
  private final PostRepository postRepository;
  private final Tag2PostRepository tag2PostRepository;

  public TagService(
      TagRepository tagRepository,
      PostRepository postRepository,
      Tag2PostRepository tag2PostRepository) {
    this.tagRepository = tagRepository;
    this.postRepository = postRepository;
    this.tag2PostRepository = tag2PostRepository;
  }

  public TagResponse getTags() {
    TagResponse tagResponse = new TagResponse();
    tagResponse.setTags(toDTOTags());
    return tagResponse;
  }

  private List<TagDto> toDTOTags() {
    List<Tag> allTags = tagRepository.findAll();
    Integer countAllPosts = postRepository.countPosts();
    Integer weights = allTags.size();

    Integer maxTag =
        allTags.stream()
            .mapToInt(v -> tag2PostRepository.countOfPostsWithTheName(v.getName()))
            .max()
            .getAsInt();

    List<TagDto> tagDtoList = new ArrayList<>();

    allTags.stream()
        .forEach(
            tag -> {
              TagDto tagDto = new TagDto();
              String tagsName = tag.getName();

              Integer countOfTagsByName =
                  tag2PostRepository.countOfPostsWithTheName(
                      tagsName); // Количество тегов с конкретным именем в постах
              Double dWeightHibernate =
                  Double.valueOf(countOfTagsByName)
                      / Double.valueOf(
                          countAllPosts); // dWeightHibernate = hibernate / weights = 4 / 20 = 0.20
              Double dWeightMax =
                  Double.valueOf(maxTag)
                      / Double.valueOf(weights); // dWeightMax = java / weights = 18 / 20 = 0.90
              Double k = 1.0 / dWeightMax; // k = 1 / dWeightMax = 1 / 0.90 = 1.11
              Double weightTag =
                  dWeightHibernate * k; // weightHibernate = hibernate * k = 0.20 * 1.11 = 0.22

              tagDto.setName(tagsName);
              tagDto.setWeight(weightTag);
              tagDtoList.add(tagDto);
            });
    return tagDtoList;
  }
}
