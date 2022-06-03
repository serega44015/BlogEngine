package main.service;

import main.dto.api.response.MyStatsResponse;
import main.model.Post;
import main.model.PostVotes;
import main.model.repositories.PostRepository;
import main.model.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class StatsService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public StatsService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public MyStatsResponse getMyStatistics(Principal principal) {
        main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
        MyStatsResponse myStatsResponse = new MyStatsResponse();
        List<Post> userPosts = userRepository.findByEmail(currentUser.getEmail()).get().getUserPosts();

        Integer postCount = userPosts.size();
        Integer likeCount = 0;
        Integer dislikeCount = 0;
        Integer viewCount = 0;
        Long firstPublication = userPosts.get(0).getTime().getTimeInMillis() / 1000;

        for (Post p : userPosts) {
            for (PostVotes pv : p.getPostVotesList()) {
                if (pv.getValue() == 1) {
                    likeCount++;
                }
                if (pv.getValue() == -1) {
                    dislikeCount++;
                }
            }
            viewCount = viewCount + p.getViewCount();
        }

        myStatsResponse.setPostsCount(postCount);
        myStatsResponse.setLikesCount(likeCount);
        myStatsResponse.setDislikesCount(dislikeCount);
        myStatsResponse.setViewsCount(viewCount);
        myStatsResponse.setFirstPublication(firstPublication);


        return myStatsResponse;
    }
}
