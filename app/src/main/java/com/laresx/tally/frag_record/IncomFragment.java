package com.laresx.tally.frag_record;

import com.laresx.tally.R;
import com.laresx.tally.db.DBManager;
import com.laresx.tally.db.TypeBean;

import java.util.List;

public class IncomFragment extends BaseRecordFragment {
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        List<TypeBean> outlist = DBManager.getTypeList(1);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("Others");
        typeIv.setImageResource(R.mipmap.in_qt_fs);

    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertItemToAccounttb(accountBean);
    }
}