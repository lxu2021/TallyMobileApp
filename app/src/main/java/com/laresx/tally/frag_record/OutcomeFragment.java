package com.laresx.tally.frag_record;

import com.laresx.tally.R;
import com.laresx.tally.db.DBManager;
import com.laresx.tally.db.TypeBean;

import java.util.List;

public class OutcomeFragment extends BaseRecordFragment{
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("Others");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);

    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBManager.insertItemToAccounttb(accountBean);
    }
}
