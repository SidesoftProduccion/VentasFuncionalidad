
OB.SSDNID = {
  AddPayments: {},
  execute: function (params, view) {
    var callback = function (rpcResponse, data, rpcRequest) {
      console.log(data);
    };

    OB.RemoteCallManager.call('ec.com.sidesoft.debitnote.interest.due.ad_actions.AddPaymentsActionHandler', {
      event: params.event,
      paymentScheduleDetailId: params.paymentScheduleDetailId,
      value: params.value,
    }, {}, callback);
  },
};

OB.SSDNID.AddPayments.onChangeInterestLatePayment = function (item, view, form, grid) {
  var paymentScheduleDetailId = item.record.id;
  var field = grid.getFieldByColumnName('EM_Ssdnid_TInterestLatePayment');
  var value = grid.getEditedCell(item.rowNum, field);
  OB.SSDNID.execute({
    event: 'onChangeInterestLatePayment',
    paymentScheduleDetailId: paymentScheduleDetailId,
    value: value,
  }, view);
};

OB.SSDNID.AddPayments.onChangeCollectionExpenses = function (item, view, form, grid) {
  var paymentScheduleDetailId = item.record.id;
  var field = grid.getFieldByColumnName('EM_Ssdnid_TCollectionExpenses');
  var value = grid.getEditedCell(item.rowNum, field);
  OB.SSDNID.execute({
    event: 'onChangeCollectionExpenses',
    paymentScheduleDetailId: paymentScheduleDetailId,
    value: value,
  }, view);
};

OB.SSDNID.AddPayments.onChangeGeneratePayment = function (item, view, form, grid) {
  var paymentScheduleDetailId = item.record.id;
  var field = grid.getFieldByColumnName('EM_Ssdnid_GeneratePayment');
  var value = grid.getEditedCell(item.rowNum, field);
  OB.SSDNID.execute({
    event: 'onChangeGeneratePayment',
    paymentScheduleDetailId: paymentScheduleDetailId,
    value: value,
  }, view);
};