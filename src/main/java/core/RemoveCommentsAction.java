package core;

import com.google.common.base.Strings;
import com.intellij.codeInsight.actions.FileInEditorProcessor;
import com.intellij.codeInsight.actions.LastRunReformatCodeOptionsProvider;
import com.intellij.codeInsight.actions.ReformatCodeRunOptions;
import com.intellij.codeInsight.actions.TextRangeType;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

/**
 * @author xiaoxi666
 * @date 2021-02-15 20:08
 * 事件监听器
 */
public class RemoveCommentsAction extends EditorAction {
    public RemoveCommentsAction() {
        super(new RemoveCommentsAction.Handler());
    }

    public static class Handler extends EditorWriteActionHandler {
        Handler() {
            super(true);
        }

        @Override
        public void executeWriteAction(Editor editor,
                                       @Nullable Caret caret,
                                       DataContext dataContext) {
            Project project = CommonDataKeys.PROJECT.getData(dataContext);
            if (project == null) {
                return;
            }
            if (!removeComments(editor)) {
                return;
            }
            reformat(editor, project);
        }

        /**
         * 检查文件最后是否为换行符（标准格式最后一行为换行符），若不是，则追加。
         * 检查的必要性说明：若最后一行不是换行符，且为注释，可能导致处理出错，具体表现为抛出java.lang.IndexOutOfBoundsException。
         * @param content
         * @return
         */
        private String checkEndLineAndModifyIfNeed(String content) {
            String lineSeparator = System.getProperty("line.separator");
            if(!content.endsWith(lineSeparator)) {
                return content + lineSeparator;
            }
            return content;
        }

        /**
         * 移除代码中的注释
         *
         * @param editor
         * @return true if remove comments successfully
         */
        private boolean removeComments(Editor editor) {
            String src = editor.getDocument().getText();
            if (Strings.isNullOrEmpty(src)) {
                return false;
            }
            String dst = CommentsRemover.doAction(checkEndLineAndModifyIfNeed(src));
            if (Strings.isNullOrEmpty(dst)) {
                return false;
            }
            editor.getDocument().setText(dst);
            return true;
        }

        /**
         * 由于我们保留了源码格式，移除注释之后会引入不必要的空格，因此需要再格式化一下
         *
         * @param editor
         * @param project
         */
        private void reformat(Editor editor, Project project) {
            PsiDocumentManager.getInstance(project).commitAllDocuments();
            PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
            if (file == null) {
                return;
            }
            LastRunReformatCodeOptionsProvider provider = new LastRunReformatCodeOptionsProvider(PropertiesComponent.getInstance());
            ReformatCodeRunOptions currentRunOptions = provider.getLastRunOptions(file);
            TextRangeType processingScope = TextRangeType.WHOLE_FILE;
            currentRunOptions.setProcessingScope(processingScope);
            (new FileInEditorProcessor(file, editor, currentRunOptions)).processCode();
        }
    }
}
