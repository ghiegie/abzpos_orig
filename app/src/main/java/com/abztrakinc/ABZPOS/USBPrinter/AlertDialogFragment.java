//package com.abztrakinc.ABZPOS.USBPrinter;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.text.InputFilter;
//import android.text.InputType;
//import android.text.Spanned;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.Spinner;
//
//import com.seikoinstruments.sdk.thermalprinter.PrinterException;
//import com.seikoinstruments.sdk.thermalprinter.PrinterManager;
//import com.abztrakinc.ABZPOS.USBPrinter.R;
//import com.seikoinstruments.sdk.thermalprinter.printerenum.*;
//
//import static com.seikoinstruments.sdk.thermalprinter.sample.MainActivity.*;
//
//
///**
// * Alert dialog fragment class.
// */
//public class AlertDialogFragment extends DialogFragment {
//
//    public static final String DIALOG = "dialog";
//    public static final String ID = "id";
//    public static final String MODEL = "model";
//
//    int dialogId;
//    int printerModel;
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialogId = getArguments().getInt(ID);
//        printerModel = getArguments().getInt(MODEL);
//        switch (dialogId) {
//            case MainActivity.DIALOG_SELECT_PRINTER_MODEL:
//                dialog = createDialogSelectPrinterModel();
//                break;
//
//            case MainActivity.DIALOG_INPUT_TEXT:
//                dialog = createDialogInputText();
//                break;
//
//            case MainActivity.DIALOG_INPUT_TEXT_EX:
//                dialog = createDialogInputTextEx();
//                break;
//
//            case MainActivity.DIALOG_PRINT_BARCODE:
//                dialog = createDialogPrintBarcode();
//                break;
//
//            case MainActivity.DIALOG_PRINT_PDF417:
//                dialog = createDialogPrintPdf417();
//                break;
//
//            case MainActivity.DIALOG_PRINT_QRCODE:
//                dialog = createDialogPrintQrcode();
//                break;
//
//            case MainActivity.DIALOG_PRINT_DATAMATRIX:
//                dialog = createDialogPrintDataMatrix();
//                break;
//
//            case MainActivity.DIALOG_PRINT_MAXICODE:
//                dialog = createDialogPrintMaxiCode();
//                break;
//
//            case MainActivity.DIALOG_PRINT_GS1STACKED:
//                dialog = createDialogPrintGs1Stacked();
//                break;
//
//            case MainActivity.DIALOG_PRINT_GS1STACKEDOMNIDIRECTIONAL:
//                dialog = createDialogPrintGs1StackedOmnidirectional();
//                break;
//
//            case MainActivity.DIALOG_PRINT_GS1EXPANDEDSTACKED:
//                dialog = createDialogPrintGs1ExpandedStacked();
//                break;
//
//            case DIALOG_PRINT_AZTECCODE:
//                dialog = createDialogPrintAztecCode();
//                break;
//
//            case DIALOG_FEED_POSITION:
//                dialog = createDialogFeedPosition();
//                break;
//
//            case MainActivity.DIALOG_CUT_PAPER:
//                dialog = createDialogCutPaper();
//                break;
//
//            case MainActivity.DIALOG_OPEN_DRAWER:
//                dialog = createDialogOpenDrawer();
//                break;
//
//            case MainActivity.DIALOG_BUZZER:
//                dialog = createDialogBuzzer();
//                break;
//
//            case MainActivity.DIALOG_EXTERNAL_BUZZER:
//                dialog = createDialogExternalBuzzer();
//                break;
//
//            case MainActivity.DIALOG_INPUT_BINARY:
//                dialog = createDialogInputBinary();
//                break;
//
//            case MainActivity.DIALOG_SEND_DATA_FILE:
//                dialog = createDialogSendDataFile();
//                break;
//
//            case MainActivity.DIALOG_SELECT_PRINTER_RESPONSE:
//                dialog = createDialogSelectPrinterResponse();
//                break;
//
//            case MainActivity.DIALOG_REGISTER_LOGO_ID1:
//                dialog = createDialogRegisterLogoID1();
//                break;
//
//            case MainActivity.DIALOG_REGISTER_LOGO_ID2:
//                dialog = createDialogRegisterLogoID2();
//                break;
//
//            case MainActivity.DIALOG_PRINT_LOGO1:
//                dialog = createDialogPrintLogoID1();
//                break;
//
//            case MainActivity.DIALOG_PRINT_LOGO2:
//                dialog = createDialogPrintLogoID2();
//                break;
//
//            case MainActivity.DIALOG_UNREGISTER_LOGO_ID1:
//                dialog = createDialogUnregisterLogoID1();
//                break;
//
//            case MainActivity.DIALOG_UNREGISTER_LOGO_ID2:
//                dialog = createDialogUnregisterLogoID2();
//                break;
//
//            case MainActivity.DIALOG_REGISTER_STYLE_SHEET_NO:
//                dialog = createDialogRegisterStyleSheetNo();
//                break;
//
//            case MainActivity.DIALOG_UNREGISTER_STYLE_SHEET_NO:
//                dialog = createDialogUnregisterStyleSheetNo();
//                break;
//
//            case MainActivity.DIALOG_SET_BARCODE_SCANNER_LISTENER:
//                dialog = createDialogSetBarcodeScannerListener();
//                break;
//
//            case MainActivity.DIALOG_SHOW_TEMPLATE:
//                dialog = createDialogShowTemplate();
//                break;
//
//            case MainActivity.DIALOG_SHOW_SLIDE:
//                dialog = createDialogShowSlide();
//                break;
//
//            case MainActivity.DIALOG_EXECUTE_MACRO:
//                dialog = createDialogExecuteMacro();
//                break;
//
//            case MainActivity.DIALOG_TURN_ON_SCREEN:
//                dialog = createDialogTurnOnScreen();
//                break;
//
//            case MainActivity.DIALOG_SELECT_TEMPLATE:
//                dialog = createDialogSelectTemplate();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_IMAGE_DATA:
//                dialog = createDialogSetTemplateImagedata();
//                break;
//
//            case MainActivity.DIALOG_SELECT_TEMPLATE_TEXT_OBJECT:
//                dialog = createDialogSelectTemplateTextObject();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_TEXT_ALIGNMENT:
//                dialog = createDialogSetTemplateTextAlignment();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_TEXT_DATA:
//                dialog = createDialogSetTemplateTextData();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_TEXT_LEFT_MARGIN:
//                dialog = createDialogSetTemplateTextLeftMargin();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_TEXT_LINE_SPACING:
//                dialog = createDialogSetTemplateTextLineSpacing();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_TEXT_BOLD:
//                dialog = createDialogSetTemplateTextBold();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_TEXT_UNDERLINE:
//                dialog = createDialogSetTemplateTextUnderLine();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_TEXT_SIZE:
//                dialog = createDialogSetTemplateTextSize();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_TEXT_FONT:
//                dialog = createDialogSetTemplateTextFont();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_TEXT_REGISTERED_FONT:
//                dialog = createDialogSetTemplateTextRegisteredFont();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_TEXT_RIGHT_SPACING:
//                dialog = createDialogsetTemplateTextRightSpacing();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_TEXT_COLOR:
//                dialog = createDialogSetTemplateTextColor();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_BARCODE_DATA:
//                dialog = createDialogSetTemplateBarcodeData();
//                break;
//
//            case MainActivity.DIALOG_SET_TEMPLATE_QRCODE_DATA:
//                dialog = createDialogSetTemplateQrcodeData();
//                break;
//
//            case MainActivity.DIALOG_REGISTER_TEMPLATE:
//                dialog = createDialogRegisterTemplate();
//                break;
//
//            case MainActivity.DIALOG_UNREGISTER_TEMPLATE:
//                dialog = createDialogUnregisterTemplate();
//                break;
//
//            case MainActivity.DIALOG_REGISTER_IMAGE_DATA:
//                dialog = createDialogRegisterImageData();
//                break;
//
//            case MainActivity.DIALOG_UNREGISTER_IMAGE_DATA:
//                dialog = createDialogUnregisterImageData();
//                break;
//
//            case MainActivity.DIALOG_REGISTER_SLIDE_DATA:
//                dialog = createDialogRegisterSlideData();
//                break;
//
//            case MainActivity.DIALOG_UNREGISTER_SLIDE_DATA:
//                dialog = createDialogUnregisterSlideData();
//                break;
//
//            case MainActivity.DIALOG_REGISTER_OPTION_FONT:
//                dialog = createDialogRegisterOptionFont();
//                break;
//
//            case MainActivity.DIALOG_CONTROL_MACRO_REGISTRATION:
//                dialog = createDialogControlMacroRegistration();
//                break;
//
//            case MainActivity.DIALOG_GET_DISPLAY_RESPONSE:
//                dialog = createDialogGetDisplayResponse();
//                break;
//
//            case MainActivity.DIALOG_FINISH_APP:
//                dialog = createDialogConfirmFinishApp();
//                break;
//
//            case MainActivity.DIALOG_BLUETOOTH_NO_SUPPORT:
//                dialog = createDialogBluetoothNoSupport();
//                break;
//
//            case MainActivity.DIALOG_ENABLE_WIFI:
//                dialog = createDialogEnableWifi();
//                break;
//        }
//        return dialog;
//    }
//
//    private Dialog createDialogSelectPrinterModel() {
//        return new AlertDialog.Builder(getActivity())
//                .setTitle(R.string.dialog_connect_title)
//                .setItems(R.array.printer_model_list,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                ((MainActivity) getActivity()).doConnect(which);
//                            }
//                        }
//                )
//                .create();
//    }
//
//    private Dialog createDialogInputText() {
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_TEXT);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(256);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.send_text_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_send_text_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        String text = editView.getText().toString();
//                        ((MainActivity) getActivity()).doSendText(text);
//                        editView.setText("");
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogInputTextEx() {
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        final View layout = inflater.inflate(R.layout.send_text_ex_layout, null);
//        ArrayAdapter<CharSequence> adapter;
//
//        final CheckBox chkReverse = (CheckBox) layout.findViewById(R.id.checkbox_reverse);
//        final Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445) {
//            chkReverse.setEnabled(false);
//            spnrAliment.setEnabled(false);
//        }
//
//        final Spinner spnrScale = (Spinner) layout.findViewById(R.id.spinner_scale);
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445) {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.char_scale_list1, android.R.layout.simple_spinner_item);
//        } else {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.char_scale_list2, android.R.layout.simple_spinner_item);
//        }
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnrScale.setAdapter(adapter);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_send_text_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtText = (EditText) layout.findViewById(R.id.edittext_text);
//                        String text = edtText.getText().toString();
//                        edtText.setText("");
//                        CheckBox chkBold = (CheckBox) layout.findViewById(R.id.checkbox_bold);
//                        CharacterBold bold;
//                        if (chkBold.isChecked()) {
//                            bold = CharacterBold.BOLD;
//                        } else {
//                            bold = CharacterBold.BOLD_CANCEL;
//                        }
//                        chkBold.setChecked(false);
//
//                        Spinner spnrUnderline = (Spinner) layout.findViewById(R.id.spinner_underline);
//                        String underlineItem = (String) spnrUnderline.getSelectedItem();
//                        CharacterUnderline underline = CharacterUnderline.valueOf(underlineItem);
//                        spnrUnderline.setSelection(0);
//
//                        CheckBox chkReverse = (CheckBox) layout.findViewById(R.id.checkbox_reverse);
//                        CharacterReverse reverse;
//                        if (chkReverse.isChecked()) {
//                            reverse = CharacterReverse.REVERSE;
//                        } else {
//                            reverse = CharacterReverse.REVERSE_CANCEL;
//                        }
//                        chkReverse.setChecked(false);
//
//                        Spinner spnrFont = (Spinner) layout.findViewById(R.id.spinner_font);
//                        String fontItem = (String) spnrFont.getSelectedItem();
//                        CharacterFont font = CharacterFont.valueOf(fontItem);
//                        spnrFont.setSelection(0);
//
//                        Spinner spnrScale = (Spinner) layout.findViewById(R.id.spinner_scale);
//                        String sizeItem = (String) spnrScale.getSelectedItem();
//                        CharacterScale scale = CharacterScale.valueOf(sizeItem);
//                        spnrScale.setSelection(0);
//
//                        Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//                        String alignmentItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(alignmentItem);
//                        spnrAliment.setSelection(0);
//
//                        ((MainActivity) getActivity()).doSendTextEx(text, bold, underline, reverse, font, scale, alignment);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogPrintBarcode() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.print_barcode_layout, null);
//        ArrayAdapter<CharSequence> adapter;
//
//        final Spinner spnrBarcodeSymbol = (Spinner) layout.findViewById(R.id.spinner_barcode_symbol);
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445) {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.barcode_symbol_list1, android.R.layout.simple_spinner_item);
//        } else if (printerModel == PrinterManager.PRINTER_MODEL_RP_E10
//                || printerModel == PrinterManager.PRINTER_MODEL_RP_D10) {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.barcode_symbol_list2, android.R.layout.simple_spinner_item);
//        } else {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.barcode_symbol_list3, android.R.layout.simple_spinner_item);
//        }
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnrBarcodeSymbol.setAdapter(adapter);
//        spnrBarcodeSymbol.setOnItemSelectedListener(new OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                EditText edtData = (EditText) layout.findViewById(R.id.edittext_data);
//                String barcodeTypeItem = (String) parent.getSelectedItem();
//                BarcodeSymbol barcodeSymbol = BarcodeSymbol.valueOf(barcodeTypeItem);
//                Spinner spnrNwRatio = (Spinner) layout.findViewById(R.id.spinner_nw_ratio);
//                spnrNwRatio.setEnabled(false);
//                if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_UPC_A)) {
//                    edtData.setText(R.string.print_barcode_upc_a_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_UPC_E)) {
//                    edtData.setText(R.string.print_barcode_upc_e_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_EAN13)) {
//                    edtData.setText(R.string.print_barcode_ean13_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_JAN13)) {
//                    edtData.setText(R.string.print_barcode_jan13_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_EAN8)) {
//                    edtData.setText(R.string.print_barcode_ean8_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_JAN8)) {
//                    edtData.setText(R.string.print_barcode_jan8_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_CODE39)) {
//                    edtData.setText(R.string.print_barcode_code39_default);
//                    spnrNwRatio.setEnabled(true);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_CODE93)) {
//                    edtData.setText(R.string.print_barcode_code93_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_CODE128)) {
//                    edtData.setText(R.string.print_barcode_code128_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_ITF)) {
//                    edtData.setText(R.string.print_barcode_itf_default);
//                    spnrNwRatio.setEnabled(true);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_CODABAR)) {
//                    edtData.setText(R.string.print_barcode_codabar_default);
//                    spnrNwRatio.setEnabled(true);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_EAN13_ADDON)) {
//                    edtData.setText(R.string.print_barcode_ean13_addon_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_JAN13_ADDON)) {
//                    edtData.setText(R.string.print_barcode_jan13_addon_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_GS1_OMNI_DIRECTIONAL)) {
//                    edtData.setText(R.string.print_barcode_gs1_omni_directional_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_GS1_TRUNCATED)) {
//                    edtData.setText(R.string.print_barcode_gs1_truncated_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_GS1_LIMITED)) {
//                    edtData.setText(R.string.print_barcode_gs1_limited_default);
//                } else if (barcodeSymbol.equals(BarcodeSymbol.BARCODE_SYMBOL_GS1_EXPANDED)) {
//                    edtData.setText(R.string.print_barcode_gs1_expanded_default);
//                } else {
//                    edtData.setText(R.string.print_barcode_upc_a_default);
//                }
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        final Spinner spnrModuleSize = (Spinner) layout.findViewById(R.id.spinner_module_size);
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445) {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.barcode_module_size_list1, android.R.layout.simple_spinner_item);
//        } else {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.barcode_module_size_list2, android.R.layout.simple_spinner_item);
//        }
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnrModuleSize.setAdapter(adapter);
//
//        final Spinner spnrNwRatio = (Spinner) layout.findViewById(R.id.spinner_nw_ratio);
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445) {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.nw_ratio_list1, android.R.layout.simple_spinner_item);
//        } else {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.nw_ratio_list2, android.R.layout.simple_spinner_item);
//        }
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnrNwRatio.setAdapter(adapter);
//
//        final Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445) {
//            spnrAliment.setEnabled(false);
//        }
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_barcode_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtData = (EditText) layout.findViewById(R.id.edittext_data);
//                        String data = edtData.getText().toString();
//                        edtData.setText("");
//
//                        Spinner spnrBarcodeSymbol = (Spinner) layout.findViewById(R.id.spinner_barcode_symbol);
//                        String barcodeTypeItem = (String) spnrBarcodeSymbol.getSelectedItem();
//                        BarcodeSymbol barcodeSymbol = BarcodeSymbol.valueOf(barcodeTypeItem);
//                        spnrBarcodeSymbol.setSelection(0);
//
//                        Spinner spnrModuleSize = (Spinner) layout.findViewById(R.id.spinner_module_size);
//                        String moduleSizeItem = (String) spnrModuleSize.getSelectedItem();
//                        ModuleSize moduleSize = ModuleSize.valueOf(moduleSizeItem);
//                        spnrModuleSize.setSelection(0);
//
//                        EditText edtHeight = (EditText) layout.findViewById(R.id.edittext_height);
//                        int moduleHeight = StringUtil.getInt(edtHeight.getText().toString(), PrinterManager.BARCODE_HEIGHT_DEFAULT);
//                        edtHeight.setText(Integer.toString(PrinterManager.BARCODE_HEIGHT_DEFAULT));
//
//                        Spinner spnrHriPosition = (Spinner) layout.findViewById(R.id.spinner_hri_position);
//                        String hriPositionItem = (String) spnrHriPosition.getSelectedItem();
//                        HriPosition hriPosition = HriPosition.valueOf(hriPositionItem);
//                        spnrHriPosition.setSelection(0);
//
//                        Spinner spnrHriFont = (Spinner) layout.findViewById(R.id.spinner_hri_font);
//                        String hriFontItem = (String) spnrHriFont.getSelectedItem();
//                        CharacterFont hriFont = CharacterFont.valueOf(hriFontItem);
//                        spnrHriFont.setSelection(0);
//
//                        Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//                        String alignmentItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(alignmentItem);
//                        spnrAliment.setSelection(0);
//
//                        Spinner spnrNwRatio = (Spinner) layout.findViewById(R.id.spinner_nw_ratio);
//                        String nwRatioItem = (String) spnrNwRatio.getSelectedItem();
//                        NwRatio nwRatio = NwRatio.valueOf(nwRatioItem);
//                        spnrNwRatio.setSelection(0);
//
//                        ((MainActivity) getActivity()).doPrintBarcode(barcodeSymbol, data, moduleSize, moduleHeight, hriPosition, hriFont, alignment, nwRatio);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogPrintPdf417() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.print_pdf417_layout, null);
//
//        final Spinner spnrErrorCorrection = (Spinner) layout.findViewById(R.id.spinner_error_correction);
//        spnrErrorCorrection.setSelection(0);
//
//        final Spinner spnrModuleSize = (Spinner) layout.findViewById(R.id.spinner_module_size);
//        ArrayAdapter<CharSequence> adapter;
//        if (printerModel == PrinterManager.PRINTER_MODEL_RP_E10 || printerModel == PrinterManager.PRINTER_MODEL_RP_D10) {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.pdf417_module_size_list2, android.R.layout.simple_spinner_item);
//        } else {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.pdf417_module_size_list1, android.R.layout.simple_spinner_item);
//        }
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnrModuleSize.setAdapter(adapter);
//        spnrModuleSize.setSelection(1);
//
//        final Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445) {
//            spnrAliment.setEnabled(false);
//        }
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_pdf417_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtData = (EditText) layout.findViewById(R.id.edittext_data);
//                        String data = edtData.getText().toString();
//                        edtData.setText("");
//
//                        Spinner spnrPDF417Symbol = (Spinner) layout.findViewById(R.id.spinner_pdf417_symbol);
//                        String symbolModeItem = (String) spnrPDF417Symbol.getSelectedItem();
//                        Pdf417Symbol pdf417Symbol = Pdf417Symbol.valueOf(symbolModeItem);
//                        spnrPDF417Symbol.setSelection(0);
//
//                        String errorCorrectionItem = (String) spnrErrorCorrection.getSelectedItem();
//                        ErrorCorrection errorCorrection = ErrorCorrection.valueOf(errorCorrectionItem);
//                        spnrErrorCorrection.setSelection(0);
//
//                        EditText edtRow = (EditText) layout.findViewById(R.id.edittext_row);
//                        int row = StringUtil.getInt(edtRow.getText().toString(), PrinterManager.PDF417_ROW_AUTO);
//                        edtRow.setText(Integer.toString(PrinterManager.PDF417_ROW_AUTO));
//
//                        EditText edtColumn = (EditText) layout.findViewById(R.id.edittext_column);
//                        int column = StringUtil.getInt(edtColumn.getText().toString(), PrinterManager.PDF417_COLUMN_AUTO);
//                        edtColumn.setText(Integer.toString(PrinterManager.PDF417_COLUMN_AUTO));
//
//                        String moduleSizeItem = (String) spnrModuleSize.getSelectedItem();
//                        ModuleSize moduleSize = ModuleSize.valueOf(moduleSizeItem);
//                        spnrModuleSize.setSelection(1);
//
//                        EditText edtModuleHeight = (EditText) layout.findViewById(R.id.edittext_module_height);
//                        int moduleHeight = StringUtil.getInt(edtModuleHeight.getText().toString(), PrinterManager.PDF417_MODULE_HEIGHT_DEFAULT);
//                        edtModuleHeight.setText(Integer.toString(PrinterManager.PDF417_MODULE_HEIGHT_DEFAULT));
//
//                        Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//                        String alignmentItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(alignmentItem);
//                        spnrAliment.setSelection(0);
//
//                        ((MainActivity) getActivity()).doPrintPdf417(
//                                data,
//                                errorCorrection,
//                                row,
//                                column,
//                                moduleSize,
//                                moduleHeight,
//                                alignment,
//                                pdf417Symbol
//                        );
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogPrintQrcode() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        final View layout = inflater.inflate(R.layout.print_qrcode_layout, null);
//
//        final Spinner spnrModuleSize = (Spinner) layout.findViewById(R.id.spinner_module_size);
//        ArrayAdapter<CharSequence> adapter;
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445
//                || printerModel == PrinterManager.PRINTER_MODEL_RP_E10
//                || printerModel == PrinterManager.PRINTER_MODEL_RP_D10) {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.qrcode_module_size_list1, android.R.layout.simple_spinner_item);
//        } else {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.qrcode_module_size_list2, android.R.layout.simple_spinner_item);
//        }
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnrModuleSize.setAdapter(adapter);
//        spnrModuleSize.setSelection(4);
//
//        final Spinner spnrModel = (Spinner) layout.findViewById(R.id.spinner_model);
//        spnrModel.setSelection(1);
//
//        final Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445) {
//            spnrAliment.setEnabled(false);
//        }
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_qrcode_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtData = (EditText) layout.findViewById(R.id.edittext_data);
//                        String data = edtData.getText().toString();
//                        edtData.setText("");
//
//                        Spinner spnrErrorCorrection = (Spinner) layout.findViewById(R.id.spinner_error_correction);
//                        String errorCorrectionItem = (String) spnrErrorCorrection.getSelectedItem();
//                        ErrorCorrection errorCorrection = ErrorCorrection.valueOf(errorCorrectionItem);
//                        spnrErrorCorrection.setSelection(0);
//
//                        String moduleSizeItem = (String) spnrModuleSize.getSelectedItem();
//                        ModuleSize moduleSize = ModuleSize.valueOf(moduleSizeItem);
//                        spnrModuleSize.setSelection(4);
//
//                        Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//                        String alignmentItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(alignmentItem);
//                        spnrAliment.setSelection(0);
//
//                        String modelItem = (String) spnrModel.getSelectedItem();
//                        QrModel model = QrModel.valueOf(modelItem);
//                        spnrModel.setSelection(1);
//
//                        ((MainActivity) getActivity()).doPrintQrcode(data, errorCorrection, moduleSize, alignment, model);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogPrintDataMatrix() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.print_datamatrix_layout, null);
//
//        final Spinner spnrDataMatrixModule = (Spinner) layout.findViewById(R.id.spinner_datamatrix_module);
//        spnrDataMatrixModule.setSelection(0);
//
//        final Spinner spnrModuleSize = (Spinner) layout.findViewById(R.id.spinner_module_size);
//        ArrayAdapter<CharSequence> adapter;
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445
//                || printerModel == PrinterManager.PRINTER_MODEL_RP_E10
//                || printerModel == PrinterManager.PRINTER_MODEL_RP_D10) {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.datamatrix_module_size_list1, android.R.layout.simple_spinner_item);
//        } else {
//            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.datamatrix_module_size_list2, android.R.layout.simple_spinner_item);
//        }
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnrModuleSize.setAdapter(adapter);
//        spnrModuleSize.setSelection(0);
//
//        final Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445) {
//            spnrAliment.setEnabled(false);
//        }
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_datamatrix_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtData = (EditText) layout.findViewById(R.id.edittext_data);
//                        String data = edtData.getText().toString();
//                        edtData.setText("");
//
//                        String dataMatrixModuleItem = (String) spnrDataMatrixModule.getSelectedItem();
//                        DataMatrixModule dataMatrixModule = DataMatrixModule.valueOf(dataMatrixModuleItem);
//                        spnrDataMatrixModule.setSelection(0);
//
//                        String moduleSizeItem = (String) spnrModuleSize.getSelectedItem();
//                        ModuleSize moduleSize = ModuleSize.valueOf(moduleSizeItem);
//                        spnrModuleSize.setSelection(0);
//
//                        Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//                        String alignmentItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(alignmentItem);
//                        spnrAliment.setSelection(0);
//
//                        ((MainActivity) getActivity()).doPrintDataMatrix(data, dataMatrixModule, moduleSize, alignment);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogPrintMaxiCode() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.print_maxicode_layout, null);
//
//        final Spinner spnrMaxiCodeMode = (Spinner) layout.findViewById(R.id.spinner_maxicode_mode);
//        spnrMaxiCodeMode.setSelection(0);
//
//        final Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445) {
//            spnrAliment.setEnabled(false);
//        }
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_maxicode_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtData = (EditText) layout.findViewById(R.id.edittext_data);
//                        String data = edtData.getText().toString();
//                        edtData.setText("");
//
//                        String maxiCodeModeItem = (String) spnrMaxiCodeMode.getSelectedItem();
//                        MaxiCodeMode maxiCodeMode = MaxiCodeMode.valueOf(maxiCodeModeItem);
//                        spnrMaxiCodeMode.setSelection(0);
//
//                        Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//                        String alignmentItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(alignmentItem);
//                        spnrAliment.setSelection(0);
//
//                        ((MainActivity) getActivity()).doPrintMaxiCode(data, maxiCodeMode, alignment);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogPrintGs1Stacked() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.print_gs1stacked_layout, null);
//
//        final Spinner spnrModuleSize = (Spinner) layout.findViewById(R.id.spinner_module_size);
//        spnrModuleSize.setSelection(0);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_gs1stacked_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtData = (EditText) layout.findViewById(R.id.edittext_data);
//                        String data = edtData.getText().toString();
//                        edtData.setText("");
//
//                        String moduleSizeItem = (String) spnrModuleSize.getSelectedItem();
//                        ModuleSize moduleSize = ModuleSize.valueOf(moduleSizeItem);
//                        spnrModuleSize.setSelection(0);
//
//                        Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//                        String alignmentItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(alignmentItem);
//                        spnrAliment.setSelection(0);
//
//                        ((MainActivity) getActivity()).doPrintGs1Stacked(data, moduleSize, alignment);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogPrintGs1StackedOmnidirectional() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.print_gs1stackedomnidir_layout, null);
//
//        final Spinner spnrModuleSize = (Spinner) layout.findViewById(R.id.spinner_module_size);
//        spnrModuleSize.setSelection(0);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_gs1stackedomnidirectional_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtData = (EditText) layout.findViewById(R.id.edittext_data);
//                        String data = edtData.getText().toString();
//                        edtData.setText("");
//
//                        String moduleSizeItem = (String) spnrModuleSize.getSelectedItem();
//                        ModuleSize moduleSize = ModuleSize.valueOf(moduleSizeItem);
//                        spnrModuleSize.setSelection(0);
//
//                        EditText edtModuleHeight = (EditText) layout.findViewById(R.id.edittext_module_height);
//                        int moduleHeight = StringUtil.getInt(edtModuleHeight.getText().toString(), PrinterManager.BARCODE_HEIGHT_DEFAULT);
//                        edtModuleHeight.setText(Integer.toString(PrinterManager.BARCODE_HEIGHT_DEFAULT));
//
//                        Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//                        String alignmentItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(alignmentItem);
//                        spnrAliment.setSelection(0);
//
//                        ((MainActivity) getActivity()).doPrintGs1StackedOmnidirectional(data, moduleHeight, moduleSize, alignment);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogPrintGs1ExpandedStacked() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.print_gs1expandedstacked_layout, null);
//
//        final Spinner spnrModuleSize = (Spinner) layout.findViewById(R.id.spinner_module_size);
//        spnrModuleSize.setSelection(0);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_gs1expandedstacked_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtData = (EditText) layout.findViewById(R.id.edittext_data);
//                        String data = edtData.getText().toString();
//                        edtData.setText("");
//
//                        String moduleSizeItem = (String) spnrModuleSize.getSelectedItem();
//                        ModuleSize moduleSize = ModuleSize.valueOf(moduleSizeItem);
//                        spnrModuleSize.setSelection(0);
//
//                        EditText edtColumn = (EditText) layout.findViewById(R.id.edittext_column);
//                        int column = StringUtil.getInt(edtColumn.getText().toString(), PrinterManager.GS1_EXPANDED_STACKED_COLUMN_DEFAULT);
//                        edtColumn.setText(Integer.toString(PrinterManager.GS1_EXPANDED_STACKED_COLUMN_DEFAULT));
//
//                        Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//                        String alignmentItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(alignmentItem);
//                        spnrAliment.setSelection(0);
//
//                        ((MainActivity) getActivity()).doPrintGs1ExpandedStacked(data, column, moduleSize, alignment);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogPrintAztecCode() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.print_azteccode_layout, null);
//
//        final Spinner spnrModuleSize = (Spinner) layout.findViewById(R.id.spinner_module_size);
//        spnrModuleSize.setSelection(4);
//
//        final Spinner spnrAztecSymbol = (Spinner) layout.findViewById(R.id.spinner_aztecsymbol);
//        spnrAztecSymbol.setSelection(0);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_azteccode_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtData = (EditText) layout.findViewById(R.id.edittext_data);
//                        String data = edtData.getText().toString();
//                        edtData.setText("");
//
//                        EditText edtLayer = (EditText) layout.findViewById(R.id.edittext_layer);
//                        int layer = StringUtil.getInt(edtLayer.getText().toString(), 0);
//                        edtLayer.setText("0");
//
//                        EditText edtErrorCorrection = (EditText) layout.findViewById(R.id.edittext_error_correction);
//                        int errorCorrection = StringUtil.getInt(edtErrorCorrection.getText().toString(), 0);
//                        edtErrorCorrection.setText("0");
//
//                        String moduleSizeItem = (String) spnrModuleSize.getSelectedItem();
//                        ModuleSize moduleSize = ModuleSize.valueOf(moduleSizeItem);
//                        spnrModuleSize.setSelection(4);
//
//                        String symbolItem = (String) spnrAztecSymbol.getSelectedItem();
//                        AztecSymbol aztecSymbol = AztecSymbol.valueOf(symbolItem);
//                        spnrAztecSymbol.setSelection(0);
//
//                        Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//                        String alignmentItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(alignmentItem);
//                        spnrAliment.setSelection(0);
//
//                        ((MainActivity) getActivity()).doPrintAztecCode(data, layer, errorCorrection, moduleSize, aztecSymbol, alignment);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogFeedPosition() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.feed_mark_position_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_feed_position_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Spinner spnrfeedPosition = (Spinner) layout.findViewById(R.id.spinner_feed_position);
//                        String feedPositionItem = (String) spnrfeedPosition.getSelectedItem();
//                        FeedPosition feedPosition = FeedPosition.valueOf(feedPositionItem);
//                        spnrfeedPosition.setSelection(0);
//
//                        ((MainActivity) getActivity()).doFeedPosition(feedPosition, feedPositionItem);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogCutPaper() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.cut_paper_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_cut_paper_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Spinner spnrCuttingMethod = (Spinner) layout.findViewById(R.id.spinner_cutting_method);
//                        String cutTypeItem = (String) spnrCuttingMethod.getSelectedItem();
//                        CuttingMethod cuttingMethod = CuttingMethod.valueOf(cutTypeItem);
//                        spnrCuttingMethod.setSelection(0);
//
//                        ((MainActivity) getActivity()).doCutPaper(cuttingMethod, cutTypeItem);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogOpenDrawer() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.open_drawer_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_open_drawer_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Spinner spnrDrawerNum = (Spinner) layout.findViewById(R.id.spinner_drawer_num);
//                        String drawerNumItem = (String) spnrDrawerNum.getSelectedItem();
//                        DrawerNum drawerNum = DrawerNum.valueOf(drawerNumItem);
//                        spnrDrawerNum.setSelection(0);
//
//                        Spinner spnrPulseWidth = (Spinner) layout.findViewById(R.id.spinner_pulse_width);
//                        String pulseWidthItem = (String) spnrPulseWidth.getSelectedItem();
//                        PulseWidth pulseWidth = PulseWidth.valueOf(pulseWidthItem);
//                        spnrPulseWidth.setSelection(0);
//
//                        ((MainActivity) getActivity()).doOpenDrawer(drawerNum, pulseWidth, drawerNumItem, pulseWidthItem);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogBuzzer() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.buzzer_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_buzzer_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtOnTime = (EditText) layout.findViewById(R.id.edittext_on_time);
//                        int onTime = StringUtil.getInt(edtOnTime.getText().toString());
//                        edtOnTime.setText("");
//
//                        EditText edtOffTime = (EditText) layout.findViewById(R.id.edittext_off_time);
//                        int offTime = StringUtil.getInt(edtOffTime.getText().toString());
//                        edtOffTime.setText("");
//
//                        ((MainActivity) getActivity()).doBuzzer(onTime, offTime);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogExternalBuzzer() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.external_buzzer_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_external_buzzer_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Spinner spnrBuzzerPattern = (Spinner) layout.findViewById(R.id.spinner_buzzer_pattern);
//                        String buzzerPatternItem = (String) spnrBuzzerPattern.getSelectedItem();
//                        BuzzerPattern buzzerPattern = BuzzerPattern.valueOf(buzzerPatternItem);
//                        spnrBuzzerPattern.setSelection(0);
//
//                        EditText edtBuzzerCount = (EditText) layout.findViewById(R.id.edittext_buzzer_count);
//                        int buzzerCount = StringUtil.getInt(edtBuzzerCount.getText().toString());
//                        edtBuzzerCount.setText("");
//
//                        ((MainActivity) getActivity()).doExternalBuzzer(buzzerPattern, buzzerCount);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogInputBinary() {
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_TEXT);
//        InputFilter[] inputFilter = new InputFilter[2];
//        inputFilter[0] = new BinaryFilter();
//        inputFilter[1] = new InputFilter.LengthFilter(256);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.send_binary_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_send_binary_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        ((MainActivity) getActivity()).doSendBinary(editView.getText().toString());
//                        editView.setText("");
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogSendDataFile() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.print_position_layout, null);
//
//        final Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//        spnrAliment.setSelection(0);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_alignment_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        String positionItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(positionItem);
//                        spnrAliment.setSelection(0);
//
//                        ((MainActivity) getActivity()).doSendDataFile(alignment);
//
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogSelectPrinterResponse() {
//        int responseList, responseValueList;
//        if (printerModel == PrinterManager.PRINTER_MODEL_DPU_S245
//                || printerModel == PrinterManager.PRINTER_MODEL_DPU_S445) {
//            responseList = R.array.printer_response_list1;
//            responseValueList = R.array.printer_response_values_list1;
//        } else if (printerModel == PrinterManager.PRINTER_MODEL_RP_E10
//                || printerModel == PrinterManager.PRINTER_MODEL_RP_D10
//                || printerModel == PrinterManager.PRINTER_MODEL_RP_FG10) {
//            responseList = R.array.printer_response_list2;
//            responseValueList = R.array.printer_response_values_list2;
//        } else {
//            responseList = R.array.printer_response_list3;
//            responseValueList = R.array.printer_response_values_list3;
//        }
//        final String[] printerResponses = getResources().getStringArray(responseValueList);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle(R.string.dialog_get_printer_response_title);
//        alertDialog.setItems(responseList, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                int id = Integer.parseInt(printerResponses[which]);
//                ((MainActivity) getActivity()).doGetPrinterResponse(id);
//            }
//        });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogRegisterLogoID1() {
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(4);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.register_Logo_id1_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_register_logo_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int logoID = StringUtil.getInt(editView.getText().toString(), 0);
//
//                        ((MainActivity) getActivity()).doRegisterLogoID1(logoID);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogRegisterLogoID2() {
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_TEXT);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(2);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.register_Logo_id2_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_register_logo_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        String logoID = editView.getText().toString();
//
//                        ((MainActivity) getActivity()).doRegisterLogoID2(logoID);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogPrintLogoID1() {
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(4);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.print_Logo_id1_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_logo_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int logoID = StringUtil.getInt(editView.getText().toString(), 0);
//
//                        ((MainActivity) getActivity()).doPrintLogoID1(logoID);
//
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogPrintLogoID2() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.print_logo_layout, null);
//
//        final Spinner spnrAliment = (Spinner) layout.findViewById(R.id.spinner_alignment);
//        spnrAliment.setSelection(0);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_print_logo_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtLogoID = (EditText) layout.findViewById(R.id.edittext_logo_id);
//                        String logoID = edtLogoID.getText().toString();
//                        edtLogoID.setText("");
//
//                        String positionItem = (String) spnrAliment.getSelectedItem();
//                        PrintAlignment alignment = PrintAlignment.valueOf(positionItem);
//                        spnrAliment.setSelection(0);
//
//                        ((MainActivity) getActivity()).doPrintLogoID2(logoID, alignment);
//
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogUnregisterLogoID1() {
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(4);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.unregister_Logo_id1_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_unregister_logo_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int logoID = StringUtil.getInt(editView.getText().toString(), 0);
//
//                        ((MainActivity) getActivity()).doUnregisterLogoID1(logoID);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogUnregisterLogoID2() {
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_TEXT);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(2);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.unregister_Logo_id2_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_unregister_logo_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        String logoID = editView.getText().toString();
//
//                        ((MainActivity) getActivity()).doUnregisterLogoID2(logoID);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogRegisterStyleSheetNo() {
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(1);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.register_style_sheet_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_register_style_sheet_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int styleSheetNo = StringUtil.getInt(editView.getText().toString(), 0);
//
//                        ((MainActivity) getActivity()).doRegisterStyleSheet(styleSheetNo);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogUnregisterStyleSheetNo() {
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(1);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.unregister_style_sheet_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_unregister_style_sheet_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int styleSheetNo = StringUtil.getInt(editView.getText().toString(), 0);
//
//                        ((MainActivity) getActivity()).doUnregisterStyleSheet(styleSheetNo);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetBarcodeScannerListener() {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.set_barcode_scanner_listener_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_set_barcode_scanner_listener_title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Spinner spnrNotationMethod = (Spinner) layout.findViewById(R.id.spinner_notation_method);
//                        String notationMethodItem = (String) spnrNotationMethod.getSelectedItem();
//                        spnrNotationMethod.setSelection(0);
//
//                        ((MainActivity) getActivity()).doSetBarcodeScannerListener(notationMethodItem);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogShowTemplate(){
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(5);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.time_ms_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_show_template_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int time = StringUtil.getInt(editView.getText().toString(), 0);
//                        ((MainActivity) getActivity()).doShowTemplate(time);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogShowSlide(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.show_slide_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.show_slide));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edt1 = (EditText) layout.findViewById(R.id.edittext1_id);
//                        int num1 = StringUtil.getInt(edt1.getText().toString());
//                        edt1.setText("");
//
//                        EditText edt2 = (EditText) layout.findViewById(R.id.edittext2_id);
//                        int num2 = StringUtil.getInt(edt2.getText().toString());
//                        edt2.setText("");
//
//                        ((MainActivity) getActivity()).doShowSlide(num1, num2);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogExecuteMacro(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.show_execute_macro_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.execute_macro));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edt1 = (EditText) layout.findViewById(R.id.edittext1_id);
//                        int num1 = StringUtil.getInt(edt1.getText().toString());
//                        edt1.setText("");
//
//                        EditText edt2 = (EditText) layout.findViewById(R.id.edittext2_id);
//                        int num2 = StringUtil.getInt(edt2.getText().toString());
//                        edt2.setText("");
//
//                        ((MainActivity) getActivity()).doExecuteMacro(num1, num2);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogTurnOnScreen(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.turn_on_screen));
//        alertDialog.setTitle(title);
//        alertDialog.setItems(R.array.is_on_list, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                ((MainActivity) getActivity()).doTurnOnScreen(which == 0);
//            }
//        });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSelectTemplate(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.select_template_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.select_template));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edt1 = (EditText) layout.findViewById(R.id.edittext1_id);
//                        int num1 = StringUtil.getInt(edt1.getText().toString());
//                        edt1.setText("");
//
//                        EditText edt2 = (EditText) layout.findViewById(R.id.edittext2_id);
//                        int num2 = StringUtil.getInt(edt2.getText().toString());
//                        edt2.setText("");
//
//                        ((MainActivity) getActivity()).doSelectTemplate(num1, num2);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateImagedata(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.set_template_image_data_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.set_template_image_data));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edt1 = (EditText) layout.findViewById(R.id.edittext1_id);
//                        int num1 = StringUtil.getInt(edt1.getText().toString());
//                        edt1.setText("");
//
//                        EditText edt2 = (EditText) layout.findViewById(R.id.edittext2_id);
//                        int num2 = StringUtil.getInt(edt2.getText().toString());
//                        edt2.setText("");
//
//                        ((MainActivity) getActivity()).doSetTemplateImageData(num1, num2);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSelectTemplateTextObject(){
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(2);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.map_id_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_select_template_text_object_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int num = StringUtil.getInt(editView.getText().toString());
//
//                        ((MainActivity) getActivity()).doSelectTemplateTextObject(num);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateTextAlignment(){
//        final String[] stringArray = getResources().getStringArray(R.array.print_alignment_list);
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle(R.string.dialog_select_template_text_alignment_title);
//        alertDialog.setItems(R.array.print_alignment_list, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                PrintAlignment alignment = PrintAlignment.valueOf(stringArray[which]);
//                ((MainActivity) getActivity()).doSetTemplateTextAlignment(alignment);
//            }
//        });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateTextData(){
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_TEXT);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(256);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.send_text_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_send_text_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        String text = editView.getText().toString();
//                        ((MainActivity) getActivity()).doSetTemplateTextData(text);
//                        editView.setText("");
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateTextLeftMargin(){
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(3);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.map_id_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_set_template_text_left_margin);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int num = StringUtil.getInt(editView.getText().toString());
//
//                        ((MainActivity) getActivity()).doSetTemplateTextLeftMargin(num);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateTextLineSpacing(){
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(3);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.line_spacing_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_set_template_text_line_spacing);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int num = StringUtil.getInt(editView.getText().toString());
//
//                        ((MainActivity) getActivity()).doSetTemplateTextLineSpacing(num);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateTextBold(){
//        final String[] stringArray = getResources().getStringArray(R.array.bold_list);
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle(R.string.dialog_set_template_text_bold_title);
//        alertDialog.setItems(R.array.bold_list, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                CharacterBold bold = CharacterBold.valueOf(stringArray[which]);
//                ((MainActivity) getActivity()).doSetTemplateTextBold(bold);
//            }
//        });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateTextUnderLine(){
//        final String[] stringArray = getResources().getStringArray(R.array.char_underline_list2);
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle(R.string.dialog_set_template_text_underline_title);
//        alertDialog.setItems(R.array.char_underline_list2, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                CharacterUnderline underline = CharacterUnderline.valueOf(stringArray[which]);
//                ((MainActivity) getActivity()).doSetTemplateTextUnderline(underline);
//            }
//        });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateTextSize(){
//        final String[] stringArray = getResources().getStringArray(R.array.char_scale_list3);
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle(R.string.dialog_set_template_text_size_title);
//        alertDialog.setItems(R.array.char_scale_list3, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                CharacterScale scale = CharacterScale.valueOf(stringArray[which]);
//                ((MainActivity) getActivity()).doSetTemplateTextSize(scale);
//            }
//        });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateTextFont(){
//        final String[] stringArray = getResources().getStringArray(R.array.char_font_list);
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle(R.string.dialog_set_template_text_font_title);
//        alertDialog.setItems(R.array.char_font_list, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                CharacterFont font = CharacterFont.valueOf(stringArray[which]);
//                ((MainActivity) getActivity()).doSetTemplateTextFont(font);
//            }
//        });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateTextRegisteredFont(){
//        final String[] stringArray = getResources().getStringArray(R.array.registered_font_list);
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle(R.string.dialog_set_template_text_registered_font_title);
//        alertDialog.setItems(R.array.registered_font_list, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                RegisteredFont font = RegisteredFont.valueOf(stringArray[which]);
//                ((MainActivity) getActivity()).doSetTemplateTextRegisteredFont(font);
//            }
//        });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogsetTemplateTextRightSpacing(){
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(3);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.right_spacing_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_set_template_text_right_spacing_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int num = StringUtil.getInt(editView.getText().toString());
//
//                        ((MainActivity) getActivity()).doSetTemplateTextRightSpacing(num);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateTextColor(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_set_template_text_color_title);
//        alertDialog.setItems(R.array.text_color_list, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                int color;
//                switch (which){
//                    case 1:  color = TEMPLATE_TEXT_COLOR_BLUE;    break;
//                    case 2:  color = TEMPLATE_TEXT_COLOR_GREEN;   break;
//                    case 3:  color = TEMPLATE_TEXT_COLOR_CYAN;    break;
//                    case 4:  color = TEMPLATE_TEXT_COLOR_RED;     break;
//                    case 5:  color = TEMPLATE_TEXT_COLOR_MAGENTA; break;
//                    case 6:  color = TEMPLATE_TEXT_COLOR_YELLOW;  break;
//                    case 7:  color = TEMPLATE_TEXT_COLOR_WHITE;   break;
//                    default: color = TEMPLATE_TEXT_COLOR_BLACK;
//                }
//                ((MainActivity) getActivity()).doSetTemplateTextColor(color);
//            }
//        });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateBarcodeData(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.set_template_barcode_data_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.set_template_barcode_data));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edt1 = (EditText) layout.findViewById(R.id.edittext1_id);
//                        int num1 = StringUtil.getInt(edt1.getText().toString());
//                        edt1.setText("");
//
//                        EditText edt2 = (EditText) layout.findViewById(R.id.edittext2_id);
//                        String num2 = edt2.getText().toString();
//                        edt2.setText("");
//
//                        ((MainActivity) getActivity()).doSetTemplateBarcodeData(num1, num2);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogSetTemplateQrcodeData(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.set_template_qrcode_data_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.set_template_qrcode_data));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        Spinner spnrQrDataMode = (Spinner) layout.findViewById(R.id.spinner_qr_data_mode);
//        spnrQrDataMode.setSelection(1);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edtString = (EditText) layout.findViewById(R.id.edittext_string);
//                        String text = edtString.getText().toString();
//                        edtString.setText("");
//
//                        EditText edtMapId = (EditText) layout.findViewById(R.id.map_id);
//                        int mapID = StringUtil.getInt(edtMapId.getText().toString());
//                        edtMapId.setText("");
//
//                        Spinner spnrModuleSize = (Spinner) layout.findViewById(R.id.spinner_module_size);
//                        String moduleSizeItem = (String)spnrModuleSize.getSelectedItem();
//                        ModuleSize moduleSize = ModuleSize.valueOf(moduleSizeItem);
//                        spnrModuleSize.setSelection(0);
//
//                        Spinner spnrErrorCorrection = (Spinner) layout.findViewById(R.id.spinner_error_correction);
//                        String errorCorrectionItem = (String)spnrErrorCorrection.getSelectedItem();
//                        ErrorCorrection errorCorrection = ErrorCorrection.valueOf(errorCorrectionItem);
//                        spnrErrorCorrection.setSelection(0);
//
//                        Spinner spnrQrDataMode = (Spinner) layout.findViewById(R.id.spinner_qr_data_mode);
//                        String qrDataModeItem = (String)spnrQrDataMode.getSelectedItem();
//                        QrDataMode mode = QrDataMode.valueOf(qrDataModeItem);
//                        spnrQrDataMode.setSelection(1);
//
//                        Spinner spnrQrQuietZone = (Spinner) layout.findViewById(R.id.spinner_qr_quiet_zone);
//                        String qrQuietZoneItem = (String)spnrQrQuietZone.getSelectedItem();
//                        QrQuietZone qrQuietZone = QrQuietZone.valueOf(qrQuietZoneItem);
//                        spnrQrQuietZone.setSelection(0);
//
//                        ((MainActivity) getActivity()).doSetTemplateQrcodeData(mapID, moduleSize, errorCorrection, mode, qrQuietZone, text);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogRegisterTemplate(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.register_template_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.register_template));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edt1 = (EditText) layout.findViewById(R.id.edittext1_id);
//                        int num1 = StringUtil.getInt(edt1.getText().toString());
//                        edt1.setText("");
//
//                        EditText edt2 = (EditText) layout.findViewById(R.id.edittext2_id);
//                        String num2 = edt2.getText().toString();
//                        edt2.setText("");
//
//                        ((MainActivity) getActivity()).doRegisterTemplate(num1, num2);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogUnregisterTemplate(){
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(3);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.template_id_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_unregister_template_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int num = StringUtil.getInt(editView.getText().toString());
//
//                        ((MainActivity) getActivity()).doUnregisterTemplate(num);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogRegisterImageData(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.register_image_data_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.register_image_data));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edt1 = (EditText) layout.findViewById(R.id.edittext1_id);
//                        int num1 = StringUtil.getInt(edt1.getText().toString());
//                        edt1.setText("");
//
//                        EditText edt2 = (EditText) layout.findViewById(R.id.edittext2_id);
//                        String num2 = edt2.getText().toString();
//                        edt2.setText("");
//
//                        ((MainActivity) getActivity()).doRegisterImageData(num1, num2);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//    private Dialog createDialogUnregisterImageData(){
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(2);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.image_id_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_unregister_image_data_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int num = StringUtil.getInt(editView.getText().toString());
//
//                        ((MainActivity) getActivity()).doUnregisterImageData(num);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogRegisterSlideData(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.register_slide_data_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.register_slide_data));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edt1 = (EditText) layout.findViewById(R.id.edittext1_id);
//                        int num1 = StringUtil.getInt(edt1.getText().toString());
//                        edt1.setText("");
//
//                        EditText edt2 = (EditText) layout.findViewById(R.id.edittext2_id);
//                        String num2 = edt2.getText().toString();
//                        edt2.setText("");
//
//                        ((MainActivity) getActivity()).doRegisterSlideData(num1, num2);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogUnregisterSlideData(){
//        final EditText editView = new EditText(getActivity());
//        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        InputFilter[] inputFilter = new InputFilter[1];
//        inputFilter[0] = new InputFilter.LengthFilter(2);
//        editView.setFilters(inputFilter);
//        editView.setText(R.string.slide_id_default);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_unregister_slide_data_title);
//        alertDialog.setView(editView);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        int num = StringUtil.getInt(editView.getText().toString());
//
//                        ((MainActivity) getActivity()).doUnregisterSlideData(num);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogRegisterOptionFont(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.register_option_font_layout, null);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.register_option_font));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText edt1 = (EditText) layout.findViewById(R.id.start_code);
//                        int startCode = StringUtil.getInt(edt1.getText().toString());
//                        edt1.setText("");
//
//                        EditText edt2 = (EditText) layout.findViewById(R.id.end_code);
//                        int endCode = StringUtil.getInt(edt2.getText().toString());
//                        edt2.setText("");
//
//                        EditText edt3 = (EditText) layout.findViewById(R.id.width);
//                        int width = StringUtil.getInt(edt3.getText().toString());
//                        edt3.setText("");
//
//                        EditText edt4 = (EditText) layout.findViewById(R.id.height);
//                        int height = StringUtil.getInt(edt4.getText().toString());
//                        edt4.setText("");
//
//                        ((MainActivity) getActivity()).doRegisterOptionFont(startCode, endCode, width, height);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogControlMacroRegistration(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.control_macro_registration_layout, null);
//
//        Spinner spnrMacroRegistrationFunction = (Spinner) layout.findViewById(R.id.spinner_macro_registration_function);
//        spnrMacroRegistrationFunction.setOnItemSelectedListener(new OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Spinner spnrMacroRegistrationFunction = (Spinner) layout.findViewById(R.id.spinner_macro_registration_function);
//                String functionItem = (String)spnrMacroRegistrationFunction.getSelectedItem();
//                MacroRegistrationFunction control = MacroRegistrationFunction.valueOf(functionItem);
//
//                EditText edtMacroId = (EditText) layout.findViewById(R.id.macro_id);
//                edtMacroId.setEnabled(control == MacroRegistrationFunction.MACRO_REGISTRATION_REGIST);
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.control_macro_registration));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Spinner spnrMacroRegistrationFunction = (Spinner) layout.findViewById(R.id.spinner_macro_registration_function);
//                        String functionItem = (String)spnrMacroRegistrationFunction.getSelectedItem();
//                        MacroRegistrationFunction control = MacroRegistrationFunction.valueOf(functionItem);
//
//                        EditText edtMacroId = (EditText) layout.findViewById(R.id.macro_id);
//                        int macroID = (control == MacroRegistrationFunction.MACRO_REGISTRATION_REGIST)?
//                                StringUtil.getInt(edtMacroId.getText().toString()): -1;
//
//                        spnrMacroRegistrationFunction.setSelection(0);
//                        edtMacroId.setText("");
//
//                        ((MainActivity) getActivity()).doControlMacroRegistration(macroID, control);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogGetDisplayResponse(){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.get_display_response_layout, null);
//        int responseList = R.array.display_response_list;
//        int responseValueList = R.array.display_response_value_list;
//        final String[] displayResponses = getResources().getStringArray(responseValueList);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        String title = String.format(getString(R.string.dialog_title_input_paramater), getString(R.string.get_display_response));
//        alertDialog.setTitle(title);
//        alertDialog.setView(layout);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Spinner spnrDisplayResponse = (Spinner) layout.findViewById(R.id.spnrDisplayResponse);
//                        int position = spnrDisplayResponse.getSelectedItemPosition();
//                        int responseID = StringUtil.getInt(displayResponses[position]);
//
//                        EditText edtContentsId = (EditText) layout.findViewById(R.id.param);
//                        int param = StringUtil.getInt(edtContentsId.getText().toString());
//                        edtContentsId.setText("");
//
//                        ((MainActivity) getActivity()).doGetDisplayResponse(responseID, param);
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//    private Dialog createDialogConfirmFinishApp() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//        alertDialog.setTitle(R.string.dialog_finish_app_title);
//        alertDialog.setMessage(R.string.dialog_finish_app_message);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        ((MainActivity) getActivity()).finishApp();
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogBluetoothNoSupport() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle(R.string.bluetooth);
//        alertDialog.setMessage(R.string.bluetooth_not_supported);
//        alertDialog.setPositiveButton(R.string.ok,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    private Dialog createDialogEnableWifi() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle(R.string.tcpip);
//        alertDialog.setMessage(R.string.enable_wifi);
//        alertDialog.setPositiveButton(R.string.allow,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        ((MainActivity) getActivity()).enableWifi();
//                    }
//                });
//        alertDialog.setNegativeButton(R.string.deny,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return alertDialog.create();
//    }
//
//
//    class BinaryFilter implements InputFilter {
//        public CharSequence filter(CharSequence source, int start, int end,
//                                   Spanned dest, int dstart, int dend) {
//
//            if (source.toString().matches("^[a-fA-F0-9]+$")) {
//                return source;
//            } else {
//                return "";
//            }
//        }
//    }
//
//
//}