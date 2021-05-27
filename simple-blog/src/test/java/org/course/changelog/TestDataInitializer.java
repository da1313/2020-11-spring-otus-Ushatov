package org.course.changelog;

//@ChangeLog
public class TestDataInitializer {

//    @ChangeSet(order = "000", id = "dropDb", author = "belfanio", runAlways = true)
//    public void dropDb(MongoDatabase mongoDatabase){
//        mongoDatabase.drop();
//    }
//
//    @ChangeSet(order = "001", id = "addTestData", author = "belfanio", runAlways = true)
//    public void addTestData(MongockTemplate mongockTemplate, DataBean dataBean){
//        Random random = new Random();
//        int userCount = 3;
//        List<User> userList = new ArrayList<>();
//        List<Post> postList = new ArrayList<>();
//        for (int i = 0; i < userCount; i++) {
//            User user = new User(String.valueOf(i), "fn" + i, "ln" + i, null, "email" + i,
//                    "pw" + i, LocalDateTime.now());
//            userList.add(user);
//            mongockTemplate.save(user);
//        }
//
//        for (int i = 0; i < 100; i++) {
//
//            User author = userList.get(random.nextInt(userCount));
//
//            int roll1 = random.nextInt(10);
//
//            boolean isActive = true;
//            if (roll1 == 0){
//                isActive = false;
//            }
//
//            if (isActive){
//
//                boolean flag = false;
//                LocalDateTime publicationTime = null;
//                int roll3 = random.nextInt(2);
//                if (roll3 == 0){
//                    publicationTime = LocalDateTime.now().plus(Duration.ofDays(random.nextInt(30)));
//                } else {
//                    flag = true;
//                    publicationTime = LocalDateTime.now().minus(Duration.ofDays(random.nextInt(30)));
//                }
//
//                int roll2 = random.nextInt(3);
//                ModerationStatus moderationStatus = null;
//                if (flag){
//                    if (roll2 == 0){
//                        moderationStatus = ModerationStatus.NEW;
//                    } else if (roll2 ==1){
//                        moderationStatus = ModerationStatus.ACCEPTED;
//                    } else {
//                        moderationStatus = ModerationStatus.DECLINED;
//                    }
//                } else {
//                    moderationStatus = ModerationStatus.NEW;
//                }
//
//                int commentCount = 0;
//                int likeCount = 0;
//                int dislikeCount = 0;
//                int viewCount = 0;
//                if (moderationStatus == ModerationStatus.ACCEPTED){
//                    commentCount = random.nextInt(userCount);
//                    likeCount = random.nextInt(userCount);
//                    dislikeCount = random.nextInt(userCount - likeCount);
//                    viewCount = random.nextInt(100);//non unique
//                }
//
//                Post post = new Post(String.valueOf(i),
//                        "title" + i,
//                        "text" + i,
//                        LocalDateTime.now(),
//                        new UserShort(author.getId(), author.getFirstName(), author.getLastName()),
//                        new PostStatistics(likeCount, dislikeCount, commentCount, viewCount),
//                        moderationStatus,
//                        publicationTime,
//                        true);
//                postList.add(post);
//                mongockTemplate.save(post);
//            } else {
//                Post post = new Post(String.valueOf(i),
//                        "title" + i,
//                        "text" + i,
//                        LocalDateTime.now(),
//                        new UserShort(author.getId(), author.getFirstName(), author.getLastName()),
//                        new PostStatistics(),
//                        ModerationStatus.NEW,
//                        null,
//                        false);
//                postList.add(post);
//                mongockTemplate.save(post);
//            }
//
//        }
//
//        dataBean.getUsers().addAll(userList);
//        dataBean.getPosts().addAll(postList);
//    }

}
