package org.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@DisplayName("PostRepository")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@Import(TestConfiguration.class)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private DataBeanImpl dataBean;

    @Test
    void findPostsSortedByViewCount() {

        List<Integer> expectedViewCountList = dataBean.getPosts().stream().sorted(Comparator
                .comparing(post -> post.getPostStatistics().getViewCount(),
                Integer::compare)).map(post -> post.getPostStatistics().getViewCount()).collect(Collectors.toList());

        List<Integer> actualViewCountList = postRepository.findAll(PageRequest.of(0, 100, Sort.by("PostStatistics.viewCount")
                .ascending())).getContent().stream().map(post -> post.getPostStatistics().getViewCount())
                .collect(Collectors.toList());

        Assertions.assertThat(actualViewCountList).isEqualTo(expectedViewCountList);

    }

    @Test
    void findByModerationStatusAndPublicationTimeLessThanEqual() {

//        LocalDateTime now = LocalDateTime.now();
//
//        List<String> expectedPostList = dataBean.getPosts().stream().filter(post -> post.getModerationStatus()
//                .equals(ModerationStatus.ACCEPTED))
//                .filter(post -> post.getPublicationTime().toEpochSecond(ZoneOffset.UTC) < now.toEpochSecond(ZoneOffset.UTC))
//                .sorted(Comparator.comparing(post -> post.getPublicationTime().toEpochSecond(ZoneOffset.UTC)))
//                .map(Post::getId)
//                .collect(Collectors.toList());
//
//        List<String> actualPostList = postRepository.findByModerationStatusAndPublicationTimeLessThanEqual(PageRequest.of(0, 100,
//                Sort.by("publicationTime")), ModerationStatus.ACCEPTED, now).getContent().stream().map(Post::getId)
//                .collect(Collectors.toList());
//
//        Assertions.assertThat(actualPostList).isEqualTo(expectedPostList);

    }
}