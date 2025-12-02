// TODO: 예시 코드 임시로 붙여넣기 한 것임. 추후 수정 필요
package facade;

import java.util.ArrayList;
import java.util.List;

import mgr.Manageable;
import mgr.Manager;

public abstract class DataEngineImpl<T extends Manageable> extends Manager<T> implements DataEngineInterface<T> {
    protected String[] headers = null;

    public void setLabels(String[] headers) {
        this.headers = headers;
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    // 테이블의 열 제목을 스트링 배열로 돌려줌
    public String[] getColumnNames() {
        return headers;
    }

    @Override
    public List<T> search(String kwd) {
        if (kwd == null)
            return mList;
        return findAll(kwd);
    }

    @Override
    public void update(String[] editTexts) {
        Manageable s = find(editTexts[0]);
        ((UIData) s).set(editTexts);
    }

    @Override
    public void remove(String kwd) {
        Manageable s = find(kwd);
        mList.remove(s);
    }

    public ArrayList<T> getAll() {
        ArrayList<T> records = new ArrayList<>();
        records.addAll(mList);
        return records;
    }

}