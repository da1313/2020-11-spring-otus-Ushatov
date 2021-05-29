package org.course.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import org.course.model.*;
import org.course.model.embedded.PostStatistics;
import org.course.model.embedded.UserShort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ChangeLog
public class DataInitializer {

    @ChangeSet(order = "000", id = "dropDb", author = "belfanio", runAlways = true)
    public void dropDb(MongoDatabase mongoDatabase){
        mongoDatabase.drop();
    }

    @ChangeSet(order = "001", id = "addTestData", author = "belfanio", runAlways = true)
    public void addUsersAndPostData(MongockTemplate mongockTemplate){
        Random random = new Random();
        int userCount = 100;
        int postCount = 500;

        List<User> userList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();
        List<Comment> commentList = new ArrayList<>();
        List<Vote> voteList = new ArrayList<>();


        Permission postRead = new Permission("1", "post-read");
        Permission postWrite = new Permission("2", "post-write");
        Permission postModerate = new Permission("3", "post-moderate");//global
        Permission postGroupModerate = new Permission("4", "post-group-moderate");//group moderator;
        Permission userRead = new Permission("5", "user-read");
        Permission userWrite = new Permission("6", "user-write");
        Permission commentRead = new Permission("7", "comment-read");
        Permission commentWrite = new Permission("8", "comment-write");
        Permission voteRead = new Permission("9", "vote-read");
        Permission voteWrite = new Permission("10", "vote-write");

        mongockTemplate.save(postRead);
        mongockTemplate.save(postWrite);
        mongockTemplate.save(postModerate);
        mongockTemplate.save(userRead);
        mongockTemplate.save(userWrite);

        Role userRole = new Role("1", "user", List.of(postRead, postWrite));
        Role moderatorRole = new Role("2", "moderator", List.of(postRead, postWrite, postModerate));//hosting moderator or for example group moderator
        Role adminRole = new Role("3", "admin", List.of(postRead, postWrite, postModerate, userRead, userWrite));

        mongockTemplate.save(userRole);
        mongockTemplate.save(moderatorRole);
        mongockTemplate.save(adminRole);

        for (int i = 0; i < userCount; i++) {
            User user = new User(String.valueOf(i), "fn" + i, "ln" + i, null, "email" + i,
                    "$2y$12$2WY2UbzCwWoBCQZdH4MMDevau1jdndHEvm/m6ciQPbqNEe01WZyG.", LocalDateTime.now(), userRole,
                    "/fake/ava.jpg", null, true);
            userList.add(user);
            mongockTemplate.save(user);
        }
        System.out.println("Loaded users");

        for (int i = 0; i < postCount; i++) {

            User author = userList.get(random.nextInt(userCount));

            int roll1 = random.nextInt(10);

            boolean isActive = true;
            if (roll1 == 0){
                isActive = false;
            }

            if (isActive){

                boolean flag = false;
                LocalDateTime publicationTime = null;
                int roll3 = random.nextInt(2);
                if (roll3 == 0){
                    publicationTime = LocalDateTime.now().plus(Duration.ofDays(random.nextInt(30)));
                } else {
                    flag = true;
                    publicationTime = LocalDateTime.now().minus(Duration.ofDays(random.nextInt(30)));
                }

                int roll2 = random.nextInt(3);
                String moderationStatus = null;
                if (flag){
                    if (roll2 == 0){
                        moderationStatus = "NEW";
                    } else if (roll2 ==1){
                        moderationStatus = "ACCEPTED";
                    } else {
                        moderationStatus = "DECLINED";
                    }
                } else {
                    moderationStatus = "NEW";
                }

                int commentCount = 0;
                int likeCount = 0;
                int dislikeCount = 0;
                int viewCount = 0;
                if (moderationStatus.equals("ACCEPTED")){
                    commentCount = random.nextInt(userCount - 1);
                    likeCount = random.nextInt(userCount - 1);
                    dislikeCount = random.nextInt(userCount - likeCount - 1);
                    viewCount = random.nextInt(100);//non unique
                }

                Post post = new Post(String.valueOf(i),
                        "Lorem ipsum dolor sit, amet consectetur adipisicing elit. Magnam consequuntur tempora iure fuga eius ex, cupiditate libero cumque soluta autem.\"" + i,
                        "Lorem ipsum dolor sit amet consectetur adipisicing elit. Hic illo animi non fugiat deserunt nostrum culpa officiis, ad, ullam exercitationem quisquam. Dignissimos illo accusamus explicabo dolore praesentium at nesciunt sequi, culpa sapiente eum impedit nulla aliquid quia minus iste, velit molestias harum qui assumenda incidunt fugiat, quasi dolorem. Earum labore corporis rerum sed perspiciatis tenetur, repudiandae laborum necessitatibus doloremque? Eligendi impedit sunt dolor, officiis amet voluptates assumenda saepe? Saepe accusantium at vel cumque nam iusto assumenda illo! Eum eaque nesciunt illo adipisci ex cumque tempore. Est debitis et, corporis exercitationem voluptas, facilis nostrum temporibus corrupti aliquam nam nobis eveniet odio!" + i,
                        LocalDateTime.now(),
                        new UserShort(author.getId(), author.getFirstName(), author.getLastName(), "/fake/ava.jpg"),
                        new PostStatistics(likeCount, dislikeCount, commentCount, viewCount),
                        moderationStatus,
                        publicationTime,
                        true,
                        "/fake/cat.jpg");
                postList.add(post);
                mongockTemplate.save(post);
            } else {
                Post post = new Post(String.valueOf(i),
                        "Lorem ipsum dolor sit, amet consectetur adipisicing elit. Magnam consequuntur tempora iure fuga eius ex, cupiditate libero cumque soluta autem." + i,
                        "Lorem ipsum dolor sit amet consectetur adipisicing elit. Hic illo animi non fugiat deserunt nostrum culpa officiis, ad, ullam exercitationem quisquam. Dignissimos illo accusamus explicabo dolore praesentium at nesciunt sequi, culpa sapiente eum impedit nulla aliquid quia minus iste, velit molestias harum qui assumenda incidunt fugiat, quasi dolorem. Earum labore corporis rerum sed perspiciatis tenetur, repudiandae laborum necessitatibus doloremque? Eligendi impedit sunt dolor, officiis amet voluptates assumenda saepe? Saepe accusantium at vel cumque nam iusto assumenda illo! Eum eaque nesciunt illo adipisci ex cumque tempore. Est debitis et, corporis exercitationem voluptas, facilis nostrum temporibus corrupti aliquam nam nobis eveniet odio!" + i,
                        LocalDateTime.now(),
                        new UserShort(author.getId(), author.getFirstName(), author.getLastName(), "/fake/ava.jpg"),
                        new PostStatistics(),
                        "NEW",
                        null,
                        false,
                        "/fake/cat.jpg");
                postList.add(post);
                mongockTemplate.save(post);
            }
        }

        System.out.println("Loaded posts");

        //comments
        int commentId = 0;
        for (Post post : postList) {
            for (int i = 0; i < post.getPostStatistics().getCommentCount(); i++) {
                boolean isSame = true;
                int roll2 = random.nextInt(20);
                boolean isEnabled = roll2 != 1;

                User user = null;
                while (isSame){
                    user = userList.get(random.nextInt(userList.size()));
                    if (!user.getId().equals(post.getUser().getId())) isSame = false;
                }

                int likes = 0;
                int dislikes = 0;
                if (isEnabled){
                    likes = random.nextInt(userList.size());
                    dislikes = random.nextInt(userList.size() - likes);
                }
                Comment comment = new Comment(String.valueOf(commentId),
                        LocalDateTime.now(),
                        "Lorem, ipsum dolor sit amet consectetur adipisicing elit. Non, minus aut? Quasi, a? Vero ratione libero est sed eius laborum beatae unde fugiat a asperiores aliquid excepturi nisi, id rerum, dolorum velit sunt qui laudantium voluptas, facilis reprehenderit. Soluta, qui." + i,
                        new UserShort(user.getId(), user.getFirstName(), user.getLastName(), "/fake/ava.jpg"),//todo remove id
                        post.getId(),
                        null,
                        isEnabled,
                        likes,
                        dislikes);
                mongockTemplate.save(comment);
                commentList.add(comment);
                commentId++;
            }
        }

        System.out.println("Load comments");

        int voteId = 0;
        for (Post post : postList) {
            for (int i = 0; i < post.getPostStatistics().getLikeCount(); i++) {
                boolean isSame = true;
                User user = null;
                while (isSame){
                    User userTmp = userList.get(random.nextInt(userList.size()));
                    if (voteList.stream().anyMatch(vote -> post.getId().equals(vote.getPostId()) && userTmp.getId().equals(vote.getUserId()))) continue;
                    if (!userTmp.getId().equals(post.getUser().getId())) {
                        isSame = false;
                        user = userTmp;
                    }
                }

                Vote vote = new Vote(String.valueOf(voteId), true, user.getId(), post.getId(), null, LocalDateTime.now());

                mongockTemplate.save(vote);
                voteList.add(vote);
                voteId++;
            }
            for (int i = 0; i < post.getPostStatistics().getDislikeCount(); i++) {
                boolean isSame = true;
                User user = null;
                while (isSame){
                    User userTmp = userList.get(random.nextInt(userList.size()));
                    if (voteList.stream().anyMatch(vote -> post.getId().equals(vote.getPostId()) && userTmp.getId().equals(vote.getUserId()))) continue;
                    if (!userTmp.getId().equals(post.getUser().getId())) {
                        isSame = false;
                        user = userTmp;
                    }
                }

                Vote vote = new Vote(String.valueOf(voteId), false, user.getId(), post.getId(), null, LocalDateTime.now());

                mongockTemplate.save(vote);
                voteList.add(vote);
                voteId++;
            }

        }

        System.out.println("Load votes");

    }

}
