# UI Elements Dictionary

Auto-generated reference of all UI element IDs for use by the E2E test suite.


## `layout/account_action.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `constraintLayoutAction` | ConstraintLayout |  |
| `icon` | ImageView |  |
| `name` | TextView | placeholder.txt |

## `layout/account_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `constraintLayout` | ConstraintLayout |  |
| `icon` | ImageView |  |
| `ticker` | ImageView |  |
| `name` | TextView | placeholder.txt |
| `account` | TextView | This is a placeholder. |
| `clean_account_local_storage_button` | ImageView |  |
| `removeButton` | ImageView |  |
| `manage_accounts_quota_bar` | ProgressBar |  |
| `manage_accounts_quota_text` | TextView | Loading quota |

## `layout/account_setup.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `login_layout` | FrameLayout |  |
| `login_background_image` | ImageView |  |
| `scroll` | ScrollView |  |
| `layout_login` | LinearLayout |  |
| `thumbnail` | ImageView | @string/app_name |
| `webfinger_layout` | LinearLayout |  |
| `webfinger_username_container` | TextInputLayout |  |
| `webfinger_username` | TextInputEditText | Username |
| `webfinger_status_text` | TextView | Testing connection |
| `webfinger_button` | AppCompatButton | Proceed |
| `main_login_layout` | LinearLayout |  |
| `centeredRefreshButton` | AppCompatButton | Check server |
| `instructions_message` | TextView | Please enter the current password |
| `hostUrlFrame` | FrameLayout |  |
| `hostUrlInput` | EditText | Server address https://… |
| `embeddedCheckServerButton` | ImageButton | Refresh connection |
| `embeddedRefreshButton` | ImageButton | Refresh connection |
| `server_status_text` | TextView | Testing connection |
| `account_username_container` | TextInputLayout |  |
| `account_username` | TextInputEditText | Username |
| `account_password_container` | TextInputLayout |  |
| `account_password` | TextInputEditText | Password |
| `auth_status_text` | TextView | Wrong username or password |
| `loginButton` | AppCompatButton | Log in |
| `welcome_link` | Button | New to %1$s? |
| `settings_link` | Button | Settings |

## `layout/activity_main.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `drawer_layout` | DrawerLayout |  |
| `nav_coordinator_layout` | include |  |

## `layout/activity_privacy_policy.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `activityPrivacyPolicyLayout` | LinearLayout |  |
| `toolbar` | include |  |
| `privacyPolicyWebview` | WebView |  |

## `layout/activity_settings.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `settings_root` | ConstraintLayout |  |
| `toolbar` | include |  |
| `settings_container` | FrameLayout |  |

## `layout/activity_storage_migration.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `migration_frame_layout` | FrameLayout |  |

## `layout/add_member_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `search_member_layout` | LinearLayout |  |
| `search_bar` | SearchView |  |
| `indeterminate_progress_bar` | ProgressBar |  |
| `members_recycler_view` | RecyclerView |  |
| `empty_data_parent` | include |  |
| `add_member_layout` | LinearLayout |  |
| `share_with_title` | TextView | Share with |
| `selected_member_layout` | include |  |
| `permissions_title` | TextView | Permissions |
| `roles_recycler_view` | RecyclerView |  |
| `options_title` | TextView | Options |
| `expiration_date_layout` | include |  |
| `confirm_action_button` | AppCompatButton | Invite member |

## `layout/add_public_link_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `create_public_link_layout` | LinearLayout |  |
| `public_link_name_title` | TextView | Name |
| `public_link_name_edit_text` | EditText | Link name |
| `permissions_title` | TextView | Permissions |
| `public_link_permissions` | include |  |
| `options_layout` | LinearLayout |  |
| `options_title` | TextView | Options |
| `password_layout` | include |  |
| `expiration_date_layout` | include |  |
| `create_public_link_button` | AppCompatButton | Create public link |

## `layout/bottom_sheet_fragment_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `item_icon` | AppCompatImageView |  |
| `item_title` | MaterialTextView | Name |
| `item_additional_icon` | ShapeableImageView |  |

## `layout/checkbox_dialog.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `checkbox_dialog` | CheckBox | Message for the checkbox |

## `layout/create_shortcut_dialog.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `create_shortcut_dialog_title` | TextView | Create a shortcut |
| `create_shortcut_dialog_url_section` | RelativeLayout |  |
| `create_shortcut_dialog_url_text` | TextView | URL |
| `create_shortcut_dialog_url_layout` | TextInputLayout |  |
| `create_shortcut_dialog_url_value` | TextInputEditText |  |
| `create_shortcut_dialog_name_file_section` | RelativeLayout |  |
| `create_shortcut_dialog_text` | TextView | Name |
| `create_shortcut_dialog_name_file_layout` | TextInputLayout |  |
| `create_shortcut_dialog_name_file_value` | TextInputEditText |  |
| `url_suffix` | TextView | .url |
| `cancelButton` | AppCompatButton | @android:string/cancel |
| `createButton` | AppCompatButton | create |

## `layout/create_space_dialog.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `create_space_dialog_title` | TextView | Create a space |
| `create_space_dialog_name_section` | ConstraintLayout |  |
| `create_space_dialog_name` | TextView | Name |
| `create_space_dialog_name_layout` | TextInputLayout |  |
| `create_space_dialog_name_value` | TextInputEditText |  |
| `create_space_dialog_subtitle_section` | ConstraintLayout |  |
| `create_space_dialog_subtitle` | TextView | Subtitle |
| `create_space_dialog_subtitle_layout` | TextInputLayout |  |
| `create_space_dialog_subtitle_value` | TextInputEditText |  |
| `create_space_dialog_quota_section` | ConstraintLayout |  |
| `create_space_dialog_quota` | TextView | Quota |
| `create_space_dialog_quota_switch` | SwitchMaterial | Space quota |
| `create_space_dialog_quota_layout` | TextInputLayout |  |
| `create_space_dialog_quota_value` | TextInputEditText |  |
| `create_space_dialog_quota_unit_label` | TextView | GB |
| `create_space_dialog_quota_no_restriction_label` | TextView | No restriction |
| `cancel_create_space_button` | AppCompatButton | Cancel |
| `create_space_button` | AppCompatButton | Create |

## `layout/dialog_file_already_exists.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `cardView` | CardView |  |
| `dialog_file_already_exists_title` | TextView | @tools:sample/lorem[8] |
| `dialog_file_already_exists_information` | TextView | @tools:sample/lorem[8] |
| `dialog_file_already_exists_checkbox` | CheckBox | Apply to all %1$s conflicts |
| `buttons` | LinearLayout |  |
| `dialog_file_already_exists_replace` | AppCompatButton | Replace |
| `dialog_file_already_exists_keep_both` | AppCompatButton | Keep both |
| `dialog_file_already_exists_skip` | AppCompatButton | Skip |

## `layout/dialog_upload_text.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `inputTextLayout` | TextInputLayout |  |
| `inputFileName` | TextInputEditText | File name |

## `layout/edit_box_dialog.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `edit_box_input_text_layout` | TextInputLayout |  |
| `user_input` | EditText |  |

## `layout/edit_share_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `shareScroll` | ScrollView |  |
| `editShareTitle` | TextView | Share with %1$s |
| `shareHeaderDivider` | View |  |
| `canEditSwitch` | SwitchCompat | can edit |
| `canEditCreateCheckBox` | CheckBox | create |
| `canEditChangeCheckBox` | CheckBox | change |
| `canEditDeleteCheckBox` | CheckBox | delete |
| `canShareSwitch` | SwitchCompat | can share |
| `private_share_error_message` | TextView |  |
| `closeButton` | AppCompatButton | sharePublicLinkCloseButton |

## `layout/errorhandling_showerror.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `errorHandlingShowErrorScrollView` | ScrollView |  |
| `errorTextView` | TextView |  |

## `layout/expiration_date_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `expiration_date_layout` | LinearLayout |  |
| `expiration_date_icon` | ImageView |  |
| `expiration_date_title` | TextView | Expiration date |
| `expiration_date_value` | TextView | This is a placeholder. |
| `expiration_date_switch` | SwitchCompat | Expiration date |

## `layout/file_details_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `fdScrollView` | NestedScrollView |  |
| `fileDetailsLayout` | ConstraintLayout |  |
| `lytName` | ConstraintLayout |  |
| `cardView` | CardView |  |
| `fdImageDetailFile` | ImageView | File Image |
| `badgeDetailFile` | ImageView |  |
| `fdname` | TextView |  |
| `fdProgressText` | TextView | Downloading… |
| `fdProgressBar` | ProgressBar |  |
| `fdCancelBtn` | ImageButton | Cancel download button |
| `divider` | MaterialDivider |  |
| `fdTypeLabel` | TextView | Type: |
| `fdType` | TextView | @tools:sample/lorem[8] |
| `fdSizeLabel` | TextView | Size: |
| `fdSize` | TextView | @tools:sample/lorem[6] |
| `divider2` | MaterialDivider |  |
| `fdModifiedLabel` | TextView | Modified: |
| `fdModified` | TextView | @tools:sample/lorem[6] |
| `fdCreatedLabel` | TextView | Created: |
| `fdCreated` | TextView | @tools:sample/lorem[6] |
| `fdLastSyncLabel` | TextView | Last sync: |
| `fdLastSync` | TextView | @tools:sample/lorem[6] |
| `divider3` | MaterialDivider |  |
| `fdPathLabel` | TextView | Location: |
| `fdPath` | TextView | @tools:sample/lorem[6] |
| `fdSpaceLabel` | TextView | Space: |
| `fdSpace` | TextView | asasa |
| `fdIconSpace` | ImageButton |  |

## `layout/file_download_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `progressText` | TextView | Not downloaded yet |
| `fdProgressBlock` | LinearLayout |  |
| `progressBar` | ProgressBar |  |
| `cancelBtn` | ImageButton |  |
| `error_image` | ImageView | Download failed |
| `errorText` | TextView | Download failed |

## `layout/file_options_bottom_sheet_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `thumbnail_bottom_sheet` | ImageView |  |
| `file_name_bottom_sheet` | TextView | File name |
| `file_size_bottom_sheet` | TextView | Size MB |
| `file_separator_bottom_sheet` | TextView | , |
| `file_last_mod_bottom_sheet` | TextView | Mod Date |
| `close_bottom_sheet` | ImageView | Close |
| `divider` | View |  |
| `scroll_bottom_sheet` | ScrollView |  |
| `file_options_bottom_sheet_layout` | LinearLayout |  |

## `layout/files_folder_picker.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `filesFolderPickerLayout` | LinearLayout |  |
| `toolbar` | include |  |
| `fragment_container` | FrameLayout |  |
| `folder_picker_no_permissions_message` | TextView | You do not have permission to add content here! |
| `folder_picker_btn_cancel` | AppCompatButton | @android:string/cancel |
| `folder_picker_btn_choose` | AppCompatButton | Choose |

## `layout/fragment_migration_choice.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `migration_choice_title` | TextView | More security for your files |
| `migration_choice_logo` | ImageView |  |
| `migration_choice_subtitle` | TextView |  |
| `migration_choice_not_enough_space_warning` | TextView | ⚠️  Your free space is currently limited on your device. Some files may not be migrated, please check that all downloaded files keep downloaded after the process has been completed. |
| `migration_choice_migrate_now_button` | Button | Migrate now |

## `layout/fragment_migration_completed.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `migration_completed_title` | TextView | More security for your files |
| `migration_completed_logo` | ImageView |  |
| `migration_completed_subtitle` | TextView | Migration completed successfully. Your files are now safer than ever. |
| `migration_completed_button` | Button | Access your files |

## `layout/fragment_migration_intro.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `migration_info_title` | TextView | More security for your files |
| `migration_info_logo` | ImageView |  |
| `migration_info_subtitle` | TextView | Your app has been updated. This app update migrates your files to a more secure location on your device. This one-time data migration is required and can take up to a minute or more. |
| `migration_info_button` | Button | Let\'s get started |

## `layout/fragment_migration_progress.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `migration_info_title` | TextView | More security for your files |
| `migration_info_logo` | ImageView |  |
| `migration_info_subtitle` | TextView | Migrating your files. Please don’t turn your device off. |
| `migration_progress` | ProgressBar |  |
| `migration_progress_button` | Button | Working… Please wait |

## `layout/fragment_transfer_list.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `transfers_recycler_view` | RecyclerView |  |
| `transfers_list_empty` | include |  |

## `layout/grid_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `ListItemLayout` | SquareLinearLayout |  |
| `thumbnail` | ImageView |  |
| `share_icons_layout` | LinearLayout |  |
| `shared_via_users_icon` | ImageView |  |
| `shared_by_link_icon` | ImageView |  |
| `localFileIndicator` | ImageView |  |
| `custom_checkbox` | ImageView |  |
| `Filename` | TextView | TextView |

## `layout/item_empty_dataset.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `list_empty_dataset_icon` | ImageView |  |
| `list_empty_dataset_title` | TextView | No files in here |
| `list_empty_dataset_sub_title` | TextView | Upload some content or sync with your devices! |

## `layout/item_file_list.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `file_list_constraint_layout` | ConstraintLayout |  |
| `icon_constraint_layout` | ConstraintLayout |  |
| `thumbnail` | ImageView |  |
| `localFileIndicator` | ImageView |  |
| `info_constraint_layout` | ConstraintLayout |  |
| `Filename` | TextView | File name |
| `share_icons_layout` | LinearLayout |  |
| `shared_via_users_icon` | ImageView |  |
| `shared_by_link_icon` | ImageView |  |
| `file_list_size` | TextView | Size MB |
| `file_list_separator` | TextView | , |
| `file_list_last_mod` | TextView | Mod Date |
| `space_path_line` | include |  |
| `options_constraint_layout` | ConstraintLayout |  |
| `custom_checkbox` | ImageView |  |
| `three_dot_menu` | ImageView |  |

## `layout/list_footer.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `footerText` | TextView |  |

## `layout/loading_dialog.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `loadingLayout` | LinearLayout |  |
| `loadingBar` | ProgressBar |  |
| `loadingText` | TextView | TextView |

## `layout/log_list_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `layout_container_activity_logs_list` | ConstraintLayout |  |
| `icon_constraint_layout` | ConstraintLayout |  |
| `imageView_icon_activity_logs_list` | ImageView |  |
| `info_constraint_layout` | ConstraintLayout |  |
| `textView_title_activity_logs_list` | TextView | @tools:sample/lorem |
| `textView_subtitle_activity_logs_list` | TextView | @tools:sample/lorem |
| `options_constraint_layout` | ConstraintLayout |  |
| `imageView_download_activity_logs_list` | ImageView |  |
| `imageView_share_activity_logs_list` | ImageView |  |
| `imageView_delete_activity_logs_list` | ImageView |  |

## `layout/logs_list_activity.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `toolbar` | include |  |
| `recyclerView_activity_logs_list` | RecyclerView |  |
| `logs_list_empty` | include |  |

## `layout/main_empty_list_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `empty_list_coordinator_layout` | CoordinatorLayout |  |
| `empty_data_parent` | include |  |

## `layout/main_file_list_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `coordinator_layout` | CoordinatorLayout |  |
| `options_layout` | SortOptionsView |  |
| `syncProgressBar` | ProgressBar |  |
| `shadow_view` | View |  |
| `swipeRefresh_main_file_list` | SwipeRefreshLayout |  |
| `fab_refresh` | ExtendedFloatingActionButton | Refresh |
| `space_header` | include |  |
| `recyclerView_main_file_list` | RecyclerView |  |
| `empty_data_parent` | include |  |
| `fab_main` | FloatingActionsMenu |  |
| `fab_upload` | FloatingActionButton | Upload |
| `fab_mkdir` | FloatingActionButton | New folder |
| `fab_newfile` | FloatingActionButton | New document |
| `fab_newshortcut` | FloatingActionButton | New shortcut |

## `layout/manage_accounts_dialog.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `indeterminate_progress_bar` | ProgressBar |  |
| `manage_accounts_layout` | LinearLayout |  |
| `cross` | ImageView | Close |
| `account_list_recycler_view` | RecyclerView |  |

## `layout/media_control.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `rewindBtn` | ImageButton | Rewind button |
| `playBtn` | ImageButton | Play or pause button |
| `forwardBtn` | ImageButton | Fast forward button |
| `currentTimeText` | TextView | 12:23:45 |
| `progressBar` | SeekBar |  |
| `totalTimeText` | TextView | 12:23:45 |

## `layout/member_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `member_item_layout` | LinearLayout |  |
| `member_icon` | ImageView |  |
| `member_name` | TextView | placeholder.txt |
| `member_role` | TextView | This is a placeholder. |
| `expiration_calendar_icon` | ImageView |  |
| `expiration_date` | TextView | This is a placeholder. |
| `edit_member_button` | ImageButton |  |
| `remove_member_button` | ImageButton |  |

## `layout/members_activity.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `members_activity_layout` | LinearLayout |  |
| `toolbar` | include |  |
| `item_header_container` | ConstraintLayout |  |
| `item_icon` | ImageView |  |
| `item_name` | TextView | placeholder.txt |
| `item_size` | TextView | 389 KB |
| `permanent_link_button` | ImageButton | Get permanent link |
| `members_fragment_container` | FrameLayout |  |

## `layout/members_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `swipe_refresh_members` | SwipeRefreshLayout |  |
| `members_section` | LinearLayout |  |
| `members_list_section` | LinearLayout |  |
| `members_title` | TextView | Members |
| `add_member_button` | ImageButton | Add new member |
| `members_recycler_view` | RecyclerView |  |
| `no_shares_message` | TextView | No data shared with users yet |
| `public_links_section` | LinearLayout |  |
| `public_links_title` | TextView | Public links |
| `add_public_link_button` | ImageButton | Add public link |
| `public_links_warning` | TextView | Anyone with the link has access to the space. |
| `public_links_recycler_view` | RecyclerView |  |
| `no_public_links_message` | TextView | No public links created yet. |

## `layout/nav_coordinator_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `app_bar_layout` | AppBarLayout |  |
| `nav_main_container` | include |  |
| `bottom_nav_view` | BottomNavigationView |  |

## `layout/nav_drawer_content.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `navigation_view_container` | NavigationView |  |
| `nav_view` | NavigationView |  |
| `nav_drawer_footer` | include |  |

## `layout/nav_drawer_footer.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `drawer_logo` | AppCompatImageView |  |
| `drawer_link_icon` | ImageView | Logo |
| `drawer_link_text` | TextView | @string/drawer_link_label |
| `account_quota_bar` | ProgressBar |  |
| `account_quota_text` | AppCompatTextView | Loading quota |
| `account_quota_status_text` | AppCompatTextView |  |

## `layout/nav_drawer_header.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `drawer_active_user` | ConstraintLayout |  |
| `drawer_header_background` | AppCompatImageView |  |
| `header` | ConstraintLayout |  |
| `drawer_current_account` | AppCompatImageView |  |
| `drawer_username` | AppCompatTextView | @string/app_name |
| `drawer_username_full` | AppCompatTextView | @string/app_name |

## `layout/nav_main_container.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `list_layout` | CoordinatorLayout |  |
| `left_fragment_container` | FrameLayout |  |
| `right_fragment_container` | FrameLayout |  |

## `layout/newfile_bottom_sheet_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `pick_doc_type_text_view` | TextView | Pick a document type to create: |
| `divider` | View |  |
| `doc_types_bottom_sheet_layout` | LinearLayout |  |

## `layout/numberkeyboard_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `key1Container` | SquareFrameLayout |  |
| `key1` | TextView | 1 |
| `key2Container` | SquareFrameLayout |  |
| `key2` | TextView | 2 |
| `key3Container` | SquareFrameLayout |  |
| `key3` | TextView | 3 |
| `key4Container` | SquareFrameLayout |  |
| `key4` | TextView | 4 |
| `key5Container` | SquareFrameLayout |  |
| `key5` | TextView | 5 |
| `key6Container` | SquareFrameLayout |  |
| `key6` | TextView | 6 |
| `key7Container` | SquareFrameLayout |  |
| `key7` | TextView | 7 |
| `key8Container` | SquareFrameLayout |  |
| `key8` | TextView | 8 |
| `key9Container` | SquareFrameLayout |  |
| `key9` | TextView | 9 |
| `biometricBtnContainer` | SquareFrameLayout |  |
| `biometricBtn` | ImageView | Biometric button |
| `key0Container` | SquareFrameLayout |  |
| `key0` | TextView | 0 |
| `backspaceBtnContainer` | SquareFrameLayout |  |
| `backspaceBtn` | ImageView | Backspace button |

## `layout/owncloud_toolbar.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `owncloud_app_bar` | AppBarLayout |  |
| `root_toolbar` | ConstraintLayout |  |
| `root_toolbar_left_icon` | AppCompatImageView | Menu |
| `root_toolbar_search_view` | SearchView |  |
| `root_toolbar_title` | AppCompatTextView | Search |
| `root_toolbar_avatar` | AppCompatImageView | Manage accounts |
| `standard_toolbar` | Toolbar |  |

## `layout/passcode_edit_text.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `passCodeEditText` | EditText |  |

## `layout/passcode_lock_activity.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `passcode_lock_layout` | ConstraintLayout |  |
| `toolbar` | include |  |
| `title` | ConstraintLayout |  |
| `passcode_header` | TextView | Please insert your passcode. |
| `passcode_explanation` | TextView | The passcode will be requested every time the app is started. |
| `passcode_value` | LinearLayout |  |
| `passcode_error` | TextView |  |
| `lock_time` | TextView |  |
| `number_keyboard` | NumberKeyboard |  |
| `left_guideline` | Guideline |  |
| `right_guideline` | Guideline |  |

## `layout/password_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `password_item_layout` | LinearLayout |  |
| `password_layout` | LinearLayout |  |
| `password_icon` | ImageView |  |
| `password_title` | TextView | Password |
| `password_value` | TextView | ***** |
| `set_password_button` | AppCompatButton | Set password |
| `remove_password_button` | AppCompatButton | Remove password |
| `set_password_switch` | SwitchCompat | Set password |

## `layout/password_policy_requirement.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `password_policy_layout` | LinearLayout |  |
| `password_policy_icon` | ImageView |  |
| `password_policy_text` | TextView |  |

## `layout/pattern_lock_activity.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `activityPatternLockLayout` | ConstraintLayout |  |
| `toolbar` | include |  |
| `pattern_header` | TextView | Please enter your pattern |
| `pattern_explanation` | TextView | The pattern will be requested every time the app is started. |
| `pattern_error` | TextView |  |
| `pattern_container` | SquareFrameLayout |  |
| `pattern_lock_view` | PatternLockView |  |

## `layout/preview_audio_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `top` | ConstraintLayout |  |
| `visual_area` | FrameLayout |  |
| `image_preview` | ImageView | Image preview |
| `image_preview_overlay` | ImageView | Image preview |
| `media_controller` | MediaControlView |  |

## `layout/preview_format_text_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `scroll_view` | ScrollView |  |
| `text_preview` | TextView |  |

## `layout/preview_image_activity.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `fragmentPager` | ViewPagerWorkAround | Image previewed |

## `layout/preview_image_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `top` | ConstraintLayout |  |
| `progressWheel` | ProgressBar |  |
| `error_image` | ImageView | Unable to display the file |
| `errorText` | TextView | Unable to display the file |
| `photo_view` | PhotoView |  |
| `message` | TextView | This is a placeholder. |
| `error_group` | Group |  |

## `layout/preview_text_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `top` | RelativeLayout |  |
| `text_layout` | include |  |
| `tab_layout` | TabLayout |  |
| `view_pager` | ViewPager2 |  |

## `layout/public_link_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `public_link_item_layout` | LinearLayout |  |
| `public_link_display_name` | TextView | placeholder.txt |
| `public_link_type` | TextView | This is a placeholder. |
| `expiration_calendar_icon` | ImageView |  |
| `expiration_date` | TextView | This is a placeholder. |
| `copy_public_link_button` | ImageButton |  |
| `edit_public_link_button` | ImageButton |  |
| `remove_public_link_button` | ImageButton |  |

## `layout/public_link_permissions.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `can_view_public_link_layout` | LinearLayout |  |
| `can_view_public_link_radio_button` | RadioButton |  |
| `can_view_public_link_icon` | ImageView |  |
| `can_view_public_link_name` | TextView | Can view |
| `can_view_public_link_description` | TextView | View, download |
| `can_edit_public_link_layout` | LinearLayout |  |
| `can_edit_public_link_radio_button` | RadioButton |  |
| `can_edit_public_link_icon` | ImageView |  |
| `can_edit_public_link_name` | TextView | Can edit |
| `can_edit_public_link_description` | TextView | View, upload, edit, download, delete |
| `secret_file_drop_public_link_layout` | LinearLayout |  |
| `secret_file_drop_public_link_radio_button` | RadioButton |  |
| `secret_file_drop_public_link_icon` | ImageView |  |
| `secret_file_drop_public_link_name` | TextView | Secret file drop |
| `secret_file_drop_public_link_description` | TextView | Upload only, existing content is not revealed |

## `layout/rate_me_dialog.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `rate_me_dialog_title_view` | TextView | Rate %1$s app! |
| `button_no_thanks` | Button | NO, THANKS |
| `button_later` | Button | LATER |
| `button_rate_now` | Button | RATE NOW |

## `layout/release_notes_activity.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `txtHeader` | TextView | New in %1$s |
| `releaseNotes` | RecyclerView |  |
| `txtFooter` | TextView | Thank you for using %1$s.\n❤ |
| `btnProceed` | Button | Proceed |

## `layout/release_notes_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `release_notes_item` | ConstraintLayout |  |
| `iconReleaseNote` | ImageView | Release note icon |
| `titleReleaseNote` | TextView | @tools:sample/lorem[40] |
| `subtitleReleaseNote` | TextView | @tools:sample/lorem[40] |

## `layout/remove_files_dialog.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `dialog_remove_thumbnail` | ImageView |  |
| `dialog_remove_information` | TextView | Do you really want to remove this item? |
| `buttons` | LinearLayout |  |
| `dialog_remove_no` | AppCompatButton | No |
| `dialog_remove_local_only` | AppCompatButton | Local only |
| `dialog_remove_yes` | AppCompatButton | Yes |

## `layout/role_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `role_item_layout` | LinearLayout |  |
| `role_radio_button` | RadioButton |  |
| `role_icon` | ImageView |  |
| `role_name` | TextView | placeholder.txt |
| `role_description` | TextView | This is a placeholder. |

## `layout/search_suggestion_row.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `edit_query` | ImageView |  |

## `layout/search_users_groups_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `search_layout` | LinearLayout |  |
| `searchView` | SearchView | Search |
| `searchUsersListView` | ListView |  |

## `layout/set_password_dialog.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `set_password_title` | TextView | Set password |
| `password_input_layout` | TextInputLayout |  |
| `password_value` | TextInputEditText |  |
| `password_generator_buttons_layout` | LinearLayout |  |
| `generate_password_button` | AppCompatButton | Generate password |
| `copy_password_button` | AppCompatButton | Copy password |
| `password_policy_intro` | TextView | Please enter a password that meets the following criteria: \n |
| `password_policy_min_characters` | include |  |
| `password_policy_upper_characters` | include |  |
| `password_policy_lower_case_characters` | include |  |
| `password_policy_min_special_characters` | include |  |
| `password_policy_max_characters` | include |  |
| `password_policy_min_digits` | include |  |
| `cancel_password_button` | AppCompatButton | Cancel |
| `set_password_button` | AppCompatButton | Set |

## `layout/set_space_icon_dialog.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `set_space_icon_title` | TextView | Set icon |
| `emoji_picker` | EmojiPickerView |  |
| `cancel_set_space_icon_button` | AppCompatButton | Cancel |

## `layout/share_activity.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `share_activity_layout` | LinearLayout |  |
| `toolbar` | include |  |
| `share_fragment_container` | FrameLayout |  |

## `layout/share_file_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `shareScroll` | ScrollView |  |
| `shareHeaderContainer` | RelativeLayout |  |
| `shareFileIcon` | ImageView |  |
| `shareFileName` | TextView | placeholder.txt |
| `shareFileSize` | TextView | 389 KB |
| `getPrivateLinkButton` | ImageButton | Get private link |
| `shareHeaderDivider` | View |  |
| `shareWithUsersSection` | LinearLayout |  |
| `shareWithUsersSectionTitle` | TextView | Users and Groups |
| `addUserButton` | ImageButton | Add share |
| `shareUsersList` | ListView |  |
| `shareNoUsers` | TextView | No data shared with users yet |
| `shareViaLinkSection` | LinearLayout |  |
| `shareWithPublicLinkTitle` | TextView | Public links |
| `addPublicLinkButton` | ImageButton | Add public link |
| `shareWarning` | TextView | Anyone with the link has access to the file/folder. |
| `sharePublicLinksList` | ListView |  |
| `shareNoPublicLinks` | TextView | No public links created yet. |

## `layout/share_public_dialog.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `publicShareDialogTitle` | TextView | Create link share |
| `shareViaLinkNameSection` | LinearLayout |  |
| `shareViaLinkNameValue` | EditText |  |
| `shareViaLinkEditPermissionGroup` | RadioGroup |  |
| `shareViaLinkEditPermissionReadOnly` | RadioButton | Download / View |
| `shareViaLinkEditPermissionReadAndWrite` | RadioButton | Download/View/Upload |
| `shareViaLinkEditPermissionUploadFiles` | RadioButton | Upload Only (File Drop) |
| `shareViaLinkPasswordSection` | RelativeLayout |  |
| `shareViaLinkPasswordSwitch` | SwitchCompat |  |
| `shareViaLinkPasswordLabel` | TextView | Password |
| `shareViaLinkPasswordValue` | EditText |  |
| `layoutPasswordGeneratorButtons` | LinearLayout |  |
| `generatePasswordButton` | AppCompatButton | Generate |
| `copyPasswordButton` | AppCompatButton | Copy |
| `shareViaLinkPasswordPolicyIntro` | TextView | Please enter a password that meets the following criteria: \n |
| `shareViaLinkPasswordPolicyMinCharacters` | LinearLayout |  |
| `shareViaLinkPasswordPolicyMinCharactersIcon` | TextView |  |
| `shareViaLinkPasswordPolicyMinCharactersText` | TextView |  |
| `shareViaLinkPasswordPolicyUpperCaseCharacters` | LinearLayout |  |
| `shareViaLinkPasswordPolicyUpperCaseCharactersIcon` | TextView |  |
| `shareViaLinkPasswordPolicyUpperCaseCharactersText` | TextView |  |
| `shareViaLinkPasswordPolicyLowerCaseCharacters` | LinearLayout |  |
| `shareViaLinkPasswordPolicyLowerCaseCharactersIcon` | TextView |  |
| `shareViaLinkPasswordPolicyLowerCaseCharactersText` | TextView |  |
| `shareViaLinkPasswordPolicyMinSpecialCharacters` | LinearLayout |  |
| `shareViaLinkPasswordPolicyMinSpecialCharactersIcon` | TextView |  |
| `shareViaLinkPasswordPolicyMinSpecialCharactersText` | TextView |  |
| `shareViaLinkPasswordPolicyMaxCharacters` | LinearLayout |  |
| `shareViaLinkPasswordPolicyMaxCharactersIcon` | TextView |  |
| `shareViaLinkPasswordPolicyMaxCharactersText` | TextView |  |
| `shareViaLinkPasswordPolicyMinDigits` | LinearLayout |  |
| `shareViaLinkPasswordPolicyMinDigitsIcon` | TextView |  |
| `shareViaLinkPasswordPolicyMinDigitsText` | TextView |  |
| `shareViaLinkExpirationSection` | RelativeLayout |  |
| `shareViaLinkExpirationSwitch` | SwitchCompat |  |
| `shareViaLinkExpirationLabel` | TextView | Expiration |
| `shareViaLinkExpirationValue` | TextView |  |
| `shareViaLinkExpirationExplanationLabel` | TextView | Expiration |
| `public_link_error_message` | TextView |  |
| `cancelButton` | AppCompatButton | Cancel |
| `saveButton` | AppCompatButton | Save |

## `layout/share_public_link_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `publicLinkName` | TextView | Username |
| `getPublicLinkButton` | ImageButton |  |
| `deletePublicLinkButton` | ImageButton |  |
| `editPublicLinkButton` | ImageButton |  |

## `layout/share_user_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `icon` | ImageView |  |
| `userOrGroupName` | TextView | Username |
| `unshareButton` | ImageButton |  |
| `editShareButton` | ImageButton |  |

## `layout/shares_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `shares_coordinator_layout` | CoordinatorLayout |  |
| `empty_data_parent` | include |  |

## `layout/sort_bottom_sheet_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `title` | TextView | Sort by |
| `divider` | View |  |
| `sort_by_name` | BottomSheetFragmentItemView | Name |
| `sort_by_date` | BottomSheetFragmentItemView | Date |
| `sort_by_size` | BottomSheetFragmentItemView | Size |

## `layout/sort_options_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `sort_options_layout` | ConstraintLayout |  |
| `sort_type_selector` | ConstraintLayout |  |
| `sort_type_title` | MaterialTextView | Name |
| `sort_type_icon` | ShapeableImageView |  |
| `view_type_selector` | AppCompatImageView | Type of view |

## `layout/space_header.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `space_header_card` | CardView |  |
| `space_header_image` | ImageView |  |
| `space_header_name` | TextView | Space name |
| `space_header_subtitle` | TextView | Space subtitle |

## `layout/space_path_line.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `space_icon` | ImageView |  |
| `space_name` | TextView | Space name |
| `path` | TextView | /path/to/file |

## `layout/spaces_list_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `coordinator_layout` | CoordinatorLayout |  |
| `sync_progress_bar` | ProgressBar |  |
| `swipe_refresh_spaces_list` | SwipeRefreshLayout |  |
| `recycler_spaces_list` | RecyclerView |  |
| `empty_data_parent` | include |  |
| `fab_create_space` | FloatingActionButton | New space |

## `layout/spaces_list_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `spaces_list_item_layout` | LinearLayout |  |
| `spaces_list_item_card` | CardView |  |
| `spaces_list_item_disabled_label` | TextView | Disabled |
| `spaces_list_item_image` | ImageView | Space image |
| `spaces_list_item_name` | TextView | Name |
| `spaces_three_dot_menu` | ImageView |  |
| `spaces_list_item_subtitle` | TextView | Subtitle |

## `layout/ssl_untrusted_cert_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `root` | LinearLayout |  |
| `header` | TextView | The identity of the server could not be verified. |
| `reason_cert_not_trusted` | TextView | - The server certificate is not trusted. |
| `reason_cert_expired` | TextView | - The server certificate expired. |
| `reason_cert_not_yet_valid` | TextView | - The server certificate valid dates are in the future. |
| `reason_hostname_not_verified` | TextView | - The URL does not match the hostname in the certificate |
| `reason_no_info_about_error` | TextView | - No information about the error |
| `details_scroll` | ScrollView |  |
| `details_view` | LinearLayout |  |
| `null_cert` | TextView | The certificate could not be shown. |
| `label_subject` | TextView | Issued to: |
| `label_subject_CN` | TextView | Common name: |
| `value_subject_CN` | TextView |  |
| `label_subject_O` | TextView | Organization: |
| `value_subject_O` | TextView |  |
| `label_subject_OU` | TextView | Organizational unit: |
| `value_subject_OU` | TextView |  |
| `label_subject_ST` | TextView | State: |
| `value_subject_ST` | TextView |  |
| `label_subject_C` | TextView | Country: |
| `value_subject_C` | TextView |  |
| `label_subject_L` | TextView | Location: |
| `value_subject_L` | TextView |  |
| `label_issuer` | TextView | Issued by: |
| `label_issuer_CN` | TextView | Common name: |
| `value_issuer_CN` | TextView |  |
| `label_issuer_O` | TextView | Organization: |
| `value_issuer_O` | TextView |  |
| `label_issuer_OU` | TextView | Organizational unit: |
| `value_issuer_OU` | TextView |  |
| `label_issuer_ST` | TextView | State: |
| `value_issuer_ST` | TextView |  |
| `label_issuer_C` | TextView | Country: |
| `value_issuer_C` | TextView |  |
| `label_issuer_L` | TextView | Location: |
| `value_issuer_L` | TextView |  |
| `label_validity` | TextView | Validity: |
| `label_validity_from` | TextView | From: |
| `value_validity_from` | TextView |  |
| `label_validity_to` | TextView | To: |
| `value_validity_to` | TextView |  |
| `label_signature` | TextView | Signature: |
| `label_signature_algorithm` | TextView | Algorithm: |
| `value_signature_algorithm` | TextView |  |
| `label_certificate_fingerprint` | TextView | Fingerprint: |
| `value_certificate_fingerprint` | TextView |  |
| `question` | TextView | Do you want to trust this certificate anyway? |
| `btnCancel` | AppCompatButton | No |
| `details_btn` | AppCompatButton | Details |
| `ok` | AppCompatButton | Yes |

## `layout/ssl_validator_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `root` | LinearLayout |  |
| `header` | TextView | The identity of the server could not be verified. |
| `reason_cert_not_trusted` | TextView | - The server certificate is not trusted. |
| `reason_cert_expired` | TextView | - The server certificate expired. |
| `reason_cert_not_yet_valid` | TextView | - The server certificate valid dates are in the future. |
| `reason_hostname_not_verified` | TextView | - The URL does not match the hostname in the certificate |
| `details_scroll` | ScrollView |  |
| `details_view` | LinearLayout |  |
| `label_subject` | TextView | Issued to: |
| `label_subject_CN` | TextView | Common name: |
| `value_subject_CN` | TextView |  |
| `label_subject_O` | TextView | Organization: |
| `value_subject_O` | TextView |  |
| `label_subject_OU` | TextView | Organizational unit: |
| `value_subject_OU` | TextView |  |
| `label_subject_ST` | TextView | State: |
| `value_subject_ST` | TextView |  |
| `label_subject_C` | TextView | Country: |
| `value_subject_C` | TextView |  |
| `label_subject_L` | TextView | Location: |
| `value_subject_L` | TextView |  |
| `label_issuer` | TextView | Issued by: |
| `label_issuer_CN` | TextView | Common name: |
| `value_issuer_CN` | TextView |  |
| `label_issuer_O` | TextView | Organization: |
| `value_issuer_O` | TextView |  |
| `label_issuer_OU` | TextView | Organizational unit: |
| `value_issuer_OU` | TextView |  |
| `label_issuer_ST` | TextView | State: |
| `value_issuer_ST` | TextView |  |
| `label_issuer_C` | TextView | Country: |
| `value_issuer_C` | TextView |  |
| `label_issuer_L` | TextView | Location: |
| `value_issuer_L` | TextView |  |
| `label_validity` | TextView | Validity: |
| `label_validity_from` | TextView | From: |
| `value_validity_from` | TextView |  |
| `label_validity_to` | TextView | To: |
| `value_validity_to` | TextView |  |
| `label_signature` | TextView | Signature: |
| `label_signature_algorithm` | TextView | Algorithm: |
| `value_signature_algorithm` | TextView |  |
| `value_signature` | TextView |  |
| `question` | TextView | Do you want to trust this certificate anyway? |
| `btnCancel` | AppCompatButton | No |
| `details_btn` | AppCompatButton | Details |
| `ok` | AppCompatButton | Yes |

## `layout/top_progress_bar.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `syncProgressBar` | ProgressBar |  |
| `shadow_view` | View |  |

## `layout/upload_bottom_sheet_fragment.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `upload_to_text_view` | TextView | Upload to %1$s |
| `divider` | View |  |
| `upload_from_files_item_view` | BottomSheetFragmentItemView |  Files  |
| `upload_from_camera_item_view` | BottomSheetFragmentItemView | Picture from camera |

## `layout/upload_list_group.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `uploadListGroupName` | MaterialTextView | Uploaded |
| `textViewFileCount` | MaterialTextView | 5 Files |
| `uploadListGroupButtonClear` | AppCompatButton | Clear |
| `uploadListGroupButtonRetry` | AppCompatButton | Retry |

## `layout/upload_list_item.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `ListItemLayout` | LinearLayout |  |
| `thumbnail` | ImageView |  |
| `upload_name` | TextView | placeholder.txt |
| `upload_file_size` | TextView | 389 KB |
| `upload_date` | TextView | 2012/05/18 12:23 PM |
| `upload_status` | TextView | Completed |
| `upload_progress_bar` | ProgressBar |  |
| `upload_account` | TextView | Username |
| `space_path_line` | include |  |
| `upload_right_button` | ImageButton |  |

## `layout/uploader_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `coordinator_layout` | CoordinatorLayout |  |
| `upload_files_layout` | LinearLayout |  |
| `toolbar` | include |  |
| `options_layout` | SortOptionsView |  |
| `empty_list_view` | include |  |
| `fragment_container` | FrameLayout |  |
| `uploader_no_permissions_message` | TextView | You do not have permission to add content here! |
| `upload_actions` | LinearLayout |  |
| `uploader_cancel` | AppCompatButton | @android:string/cancel |
| `uploader_choose_folder` | AppCompatButton | Upload |

## `layout/uploader_list_item_layout.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `thumbnail` | ImageView |  |
| `filename` | TextView |  |
| `file_size` | TextView | Size MB |
| `file_separator` | TextView | ,  |
| `last_mod` | TextView | Mod Date |

## `layout/video_preview.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `root` | FrameLayout |  |
| `video_player` | PlayerView |  |

## `layout/whats_new_activity.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `contentPanel` | ViewPager |  |
| `skip` | AppCompatButton | Skip |
| `progressIndicator` | ProgressIndicator |  |
| `forward` | ImageButton |  |

## `layout/whats_new_element.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `whatsNewImage` | ImageView |  |
| `whatsNewTitle` | TextView |  |
| `whatsNewText` | TextView |  |

## `menu/bottom_navbar_menu.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `nav_all_files` | item | Files |
| `nav_shared_by_link_files` | item | Links |
| `nav_spaces` | item | @string/bottom_nav_spaces |
| `nav_uploads` | item | Uploads |
| `nav_available_offline_files` | item | Offline |

## `menu/drawer_menu.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `drawer_menu_settings_etc` | group |  |
| `nav_settings` | item | Settings |
| `drawer_menu_feedback` | item | Feedback |
| `drawer_menu_help` | item | Help |
| `drawer_menu_privacy_policy` | item | Privacy policy |
| `footer_spacer_1` | item |  |
| `footer_spacer_2` | item |  |

## `menu/file_actions_menu.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `file_action_select_all` | item | Select all |
| `action_select_inverse` | item | Select inverse |
| `action_share_file` | item | Share |
| `action_open_file_with` | item | Open with |
| `action_download_file` | item | Download |
| `action_sync_file` | item | Synchronize |
| `action_cancel_sync` | item | Cancel sync |
| `action_rename_file` | item | Rename |
| `action_move` | item | Move |
| `action_copy` | item | @android:string/copy |
| `action_send_file` | item | Send |
| `action_set_available_offline` | item | Set as available offline |
| `action_unset_available_offline` | item | Unset as available offline |
| `action_see_details` | item | Details |
| `action_remove_file` | item | Remove |

## `menu/main_menu.xml`

| Element ID | Element Type | Description |
|---|---|---|
| `action_search` | item | Search this folder |
| `action_select_all` | item | Select all |
| `action_share_current_folder` | item | Share |
