package main.service;

import main.dto.TagDto;
import main.dto.api.response.TagResponse;
import main.model.Tag;
import main.model.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static main.mappers.converter.ResultValue.ZERO;

@Service
public class TagService {
  private final TagRepository tagRepository;

  public TagService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  public TagResponse getTags() {
    List<Tag> allTags = tagRepository.findAll();
    TagResponse tagResponse = new TagResponse();
    List<TagDto> allTagsDto = new ArrayList<>();

    if (allTags.size() > ZERO) {
      Tag maxTag =
          allTags.stream().max(Comparator.comparing(tag -> tag.getPostWithTags().size())).get();
      Double tagsCount = Double.valueOf(allTags.size());
      Double k = 1.0 / (maxTag.getPostWithTags().size() / tagsCount);
      allTags.forEach(
          t -> {
            TagDto tagDto = new TagDto();
            tagDto.setName(t.getName());
            tagDto.setWeight(t.getPostWithTags().size() / tagsCount * k);
            allTagsDto.add(tagDto);
          });
    }
    tagResponse.setTags(allTagsDto);
    return tagResponse;
  }
}
