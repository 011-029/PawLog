package mgr;

import core.User;

import java.util.ArrayList;

public abstract class PetRecordMgr<T extends PetOwned & Manageable>
        extends Manager<T> {

    protected int nextIndexId = -1;

    public void printByOwner(String ownerId) {
        for (T m : mList) {
            // 유저 ID 당 등록된 펫이 한 마리라고 가정
            if (m.getOwnerId().equals(ownerId))
                m.print();
        }
    }

    public ArrayList<T> getAllByOwner(User u) {
        ArrayList<T> result = new ArrayList<>();
        for (T m : mList) {
            if (m.getOwnerId().equals(u.getId()))
                result.add(m);
        }
        return result;
    }

    public T findByIndexId(int indexId) {
        // 인덱스 번호로 객체 찾기
        for (T m : mList) {
            if (m.getIndexId() == indexId)
                return m;
        }
        return null;
    }

    public boolean removeByIndexId(int indexId) {
        // 삭제된 객체가 있으면 true, 없으면 false 반환
        boolean removed = mList.removeIf(m -> m.getIndexId() == indexId);
        if (removed)
            saveToFile(getFilePath());
        return removed;
    }

    protected void initNextIndexId() {
        int max = 0;
        for (T m : mList) {
            if (m.getIndexId() > max)
                max = m.getIndexId();
        }
        nextIndexId = max + 1;
    }

    protected void saveWithIndexId(T record) {
        record.setIndexId(nextIndexId++);
        mList.add(record);
        saveToFile(getFilePath());
    }

    protected abstract String getFilePath();
}
