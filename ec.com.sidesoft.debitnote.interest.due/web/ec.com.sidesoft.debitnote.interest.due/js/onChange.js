
OB.SSDNID = {
  AddPayments: {},
  execute: function (params, view) {
    var callback = function (rpcResponse, data, rpcRequest) {
      console.log(data);
    };

    OB.RemoteCallManager.call('ec.com.sidesoft.debitnote.interest.due.ad_actions.AddPaymentsActionHandler', {
      event: params.event,
      invoiceNo: params.invoiceNo,
      expectedDate: params.expectedDate,
      value: params.value,
    }, {}, callback);
  },
};

OB.SSDNID.AddPayments.onChangeInterestLatePayment = function (item, view, form, grid) {
  var invoiceNo = item.record.invoiceNo;
  var expectedDate = item.record.expectedDate;
  var field = grid.getFieldByColumnName('EM_Ssdnid_TInterestLatePayment');
  var value = grid.getEditedCell(item.rowNum, field);
  OB.SSDNID.execute({
    event: 'onChangeInterestLatePayment',
    invoiceNo: invoiceNo,
    expectedDate: expectedDate,
    value: value,
  }, view);
};

OB.SSDNID.AddPayments.onChangeCollectionExpenses = function (item, view, form, grid) {
  var invoiceNo = item.record.invoiceNo;
  var expectedDate = item.record.expectedDate;
  var field = grid.getFieldByColumnName('EM_Ssdnid_TCollectionExpenses');
  var value = grid.getEditedCell(item.rowNum, field);
  OB.SSDNID.execute({
    event: 'onChangeCollectionExpenses',
    invoiceNo: invoiceNo,
    expectedDate: expectedDate,
    value: value,
  }, view);
};

OB.SSDNID.AddPayments.onChangeGeneratePayment = function (item, view, form, grid) {
  var invoiceNo = item.record.invoiceNo;
  var expectedDate = item.record.expectedDate;
  var field = grid.getFieldByColumnName('EM_Ssdnid_GeneratePayment');
  var value = grid.getEditedCell(item.rowNum, field);
  OB.SSDNID.execute({
    event: 'onChangeGeneratePayment',
    invoiceNo: invoiceNo,
    expectedDate: expectedDate,
    value: value,
  }, view);
};