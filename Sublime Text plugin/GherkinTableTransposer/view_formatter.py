from .gherkin_table import GherkinTable


class ViewFormatter(object):

    def __init__(self, sublime, view):
        self._sublime = sublime
        self._view = view

    def format_view(self, edit):
        self._store_caret_position()
        self._format_and_replace(edit)
        self._reset_caret_position()

    def _format_and_replace(self, edit):
        for region in self._view.sel():
            if not region.empty():
                self._format_region(region, edit)
            else:
                entire_buffer = self._sublime.Region(0, self._view.size())
                self._format_region(entire_buffer, edit)

    def _format_region(self, region, edit):
        table = GherkinTable(self._view.substr(region))
        table.transpose()
        self._view.replace(edit, region, str(table))

    def _store_caret_position(self):
        self._pos = self._view.sel()[0].begin()

    def _reset_caret_position(self):
        self._view.sel().clear()
        self._view.sel().add(self._sublime.Region(self._pos, self._pos))
