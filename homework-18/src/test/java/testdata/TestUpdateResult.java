package testdata;

import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;

public class TestUpdateResult extends UpdateResult {

    private final long modifiedCount;

    public TestUpdateResult(long modifiedCount) {
        this.modifiedCount = modifiedCount;
    }

    @Override
    public boolean wasAcknowledged() {
        return false;
    }

    @Override
    public long getMatchedCount() {
        return 0;
    }

    @Override
    public long getModifiedCount() {
        return modifiedCount;
    }

    @Override
    public BsonValue getUpsertedId() {
        return null;
    }
}
