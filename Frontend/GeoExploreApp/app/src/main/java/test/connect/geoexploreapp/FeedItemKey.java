package test.connect.geoexploreapp;

import java.util.Objects;

public class FeedItemKey {
    private String type;
    private Long postId;
    public FeedItemKey(String type, Long postId) {
        this.type = type;
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedItemKey that = (FeedItemKey) o;
        return Objects.equals(type, that.type) && Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, postId);
    }

}
