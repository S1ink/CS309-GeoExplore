package test.connect.geoexploreapp.model;

public class Comment {
    private Long id;
    private String userId;
    private String comment;
    private Long postId;
    private String postType;

    public Comment(String adduserId, Long addpostid, String type, String Addcomment) {
        this.userId = adduserId;
        this.postId = addpostid;
        this.postType = type;
        this.comment = Addcomment;

    }

    public Comment( String Userid,Long postId) {
        this.userId = Userid;
        this.postId = postId;
    }

    public Comment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getPostid() {
        return postId;
    }

    public void setPostid(Long postid) {
        this.postId = postid;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }
}
