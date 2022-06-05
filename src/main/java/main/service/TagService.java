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

    List<TagDto> tagDtoList = toDTOTags();
    tagResponse.setTags(tagDtoList);
    return tagResponse;
  }

  private List<TagDto> toDTOTags() {
    List<Tag> allTags = tagRepository.findAll();
    int countAllPosts = postRepository.countPosts();
    int weights = allTags.size();

    int maxTag =
        allTags.stream()
            .mapToInt(v -> tag2PostRepository.countOfPostsWithTheName(v.getName()))
            .max()
            .getAsInt();

    List<TagDto> tagDtoList = new ArrayList<>();

    for (int a = 0; a < allTags.size(); a++) {
      TagDto tagDto = new TagDto();
      String tagsName = allTags.get(a).getName();

      int countOfTagsByName =
          tag2PostRepository.countOfPostsWithTheName(
              tagsName); // Количество тегов с конкретным именем в постах
      double dWeightHibernate =
          Double.valueOf(countOfTagsByName)
              / Double.valueOf(
                  countAllPosts); // dWeightHibernate = hibernate / weights = 4 / 20 = 0.20
      double dWeightMax =
          Double.valueOf(maxTag)
              / Double.valueOf(weights); // dWeightMax = java / weights = 18 / 20 = 0.90
      double k = 1.0 / dWeightMax; // k = 1 / dWeightMax = 1 / 0.90 = 1.11
      double weightTag =
          dWeightHibernate * k; // weightHibernate = hibernate * k = 0.20 * 1.11 = 0.22

      tagDto.setName(tagsName);
      tagDto.setWeight(weightTag);
      tagDtoList.add(tagDto);
    }

    return tagDtoList;
  }
}
