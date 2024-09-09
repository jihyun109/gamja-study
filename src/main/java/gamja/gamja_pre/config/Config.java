package gamja.gamja_pre.config;

import gamja.gamja_pre.domain.service.PostService;
import gamja.gamja_pre.domain.repository.PostRepository;
import org.springframework.context.annotation.Bean;

// JPA 가 자동으로 JpaRepository 를 스프링 빈으로 자동 등록해줌.
public class Config {
    private final PostRepository postRepository;

    public Config(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Bean
    public PostService postService() {
        return new PostService(postRepository);
    }
}
