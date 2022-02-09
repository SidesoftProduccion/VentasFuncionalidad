isc.defineClass('OBEXAPP_GenerateDebitNotePopup', isc.OBPopup);
isc.OBEXAPP_GenerateDebitNotePopup.addProperties({
  width: 320,
  height: 200,
  title: null,
  showMinimizeButton: false,
  showMaximizeButton: false,

  view: null,
  params: null,
  actionHandler: null,

  mainform: null,
  okButton: null,
  cancelButton: null,

  initWidget: function () {
    this.mainform = isc.Label.create({
      height: 1,
      width: 300,
      overflow: 'visible',
      align: "center",
      valign: "center",
      contents: '¿Está de acuerdo en ejecutar esta operación? '
    });

    this.okButton = isc.OBFormButton.create({
      title: 'Aceptar',
      popup: this,
      action: function () {
        var callback = function (rpcResponse, data, rpcRequest) {
          console.log(data);
          if (data.result == 'OK') {
            isc.say('Nota de débito generada con éxito');
          } else {
            isc.say('Error al intentar generar nota débito');
          }

          rpcRequest.clientContext.popup.closeClick();
        };

        OB.RemoteCallManager.call(this.popup.actionHandler, {
          rows: this.popup.rows,
        }, {}, callback, { popup: this.popup });
      }
    });

    this.cancelButton = isc.OBFormButton.create({
      title: 'Cancelar',
      popup: this,
      action: function () {
        this.popup.closeClick();
      }
    });

    this.items = [
      isc.VLayout.create({
        defaultLayoutAlign: "center",
        align: "center",
        width: "100%",
        layoutMargin: 10,
        membersMargin: 6,
        members: [
          isc.HLayout.create({
            defaultLayoutAlign: "center",
            align: "center",
            layoutMargin: 30,
            membersMargin: 6,
            members: this.mainform
          }),
          isc.HLayout.create({
            defaultLayoutAlign: "center",
            align: "center",
            membersMargin: 10,
            members: [this.okButton, this.cancelButton]
          })
        ]
      })
    ];

    this.Super('initWidget', arguments);
  }

});

OB.OBEXAPP = OB.OBEXAPP || {};
OB.OBEXAPP.SSDNID = {
  execute: function (params, view) {
    var i, selection = params.button.contextView.viewGrid.getSelectedRecords(), rows = [];

    for (i = 0; i < selection.length; i++) {
      rows.push(selection[i][OB.Constants.ID]);
    };

    isc.OBEXAPP_GenerateDebitNotePopup.create({
      view: view,
      params: params,
      actionHandler: 'ec.com.sidesoft.debitnote.interest.due.ad_actions.GenerateDebitNoteActionHandler',
      rows: rows,
    }).show();

  },

  generateDebitNote: function (params, view) {
    OB.OBEXAPP.SSDNID.execute(params, view);
  },
};