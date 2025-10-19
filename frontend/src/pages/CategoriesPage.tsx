import { useEffect, useMemo, useState } from 'react';
import toast from 'react-hot-toast';
import { createCategory, deleteCategory, fetchCategories, updateCategory } from '../api/categories';
import { Category, CategoryPayload } from '../types';
import { CategoryForm } from '../components/CategoryForm';
import { PageContainer } from '../components/PageContainer';

export const CategoriesPage: React.FC = () => {
  const [categories, setCategories] = useState<Category[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingCategory, setEditingCategory] = useState<Category | null>(null);

  useEffect(() => {
    const loadCategories = async () => {
      setIsLoading(true);
      try {
        const data = await fetchCategories();
        setCategories(data);
      } catch (error) {
        toast.error('Failed to load categories.');
        console.error(error);
      } finally {
        setIsLoading(false);
      }
    };

    loadCategories();
  }, []);

  const handleCreate = async (payload: CategoryPayload) => {
    await toast.promise(createCategory(payload), {
      loading: 'Creating category...',
      success: 'Category created.',
      error: 'Failed to create category.'
    });
    const data = await fetchCategories();
    setCategories(data);
    setShowForm(false);
  };

  const handleUpdate = async (payload: CategoryPayload) => {
    if (!editingCategory) return;
    await toast.promise(updateCategory(editingCategory.id, payload), {
      loading: 'Updating category...',
      success: 'Category updated.',
      error: 'Failed to update category.'
    });
    const data = await fetchCategories();
    setCategories(data);
    setEditingCategory(null);
    setShowForm(false);
  };

  const handleDelete = async (category: Category) => {
    const confirmed = window.confirm(`Delete category "${category.name}"?`);
    if (!confirmed) return;

    await toast.promise(deleteCategory(category.id), {
      loading: 'Deleting category...',
      success: 'Category deleted.',
      error: 'Failed to delete category.'
    });
    setCategories((prev) => prev.filter((item) => item.id !== category.id));
  };

  const formTitle = useMemo(
    () => (editingCategory ? `Edit ${editingCategory.name}` : 'Create category'),
    [editingCategory]
  );

  return (
    <PageContainer
      title="Categories"
      actions={
        <button
          onClick={() => {
            setShowForm(true);
            setEditingCategory(null);
          }}
          className="rounded-md bg-primary px-4 py-2 text-sm font-semibold text-white shadow hover:bg-primary-dark"
        >
          New Category
        </button>
      }
    >
      {isLoading ? (
        <p className="text-sm text-slate-600">Loading categories...</p>
      ) : (
        <div className="space-y-6">
          {showForm && (
            <div className="rounded-lg border border-slate-200 p-4">
              <div className="mb-4 flex items-center justify-between">
                <h2 className="text-lg font-semibold text-slate-800">{formTitle}</h2>
                <button className="text-sm text-slate-500 hover:text-slate-700" onClick={() => setShowForm(false)}>
                  Close
                </button>
              </div>
              <CategoryForm
                initial={editingCategory}
                onSubmit={editingCategory ? handleUpdate : handleCreate}
                onCancel={() => {
                  setShowForm(false);
                  setEditingCategory(null);
                }}
              />
            </div>
          )}

          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-slate-200">
              <thead className="bg-slate-50">
                <tr>
                  <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-600">
                    Name
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-600">
                    Description
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-semibold uppercase tracking-wider text-slate-600">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 bg-white">
                {categories.map((category) => (
                  <tr key={category.id}>
                    <td className="whitespace-nowrap px-4 py-3 text-sm font-medium text-slate-800">{category.name}</td>
                    <td className="px-4 py-3 text-sm text-slate-600">{category.description ?? '—'}</td>
                    <td className="flex justify-end gap-2 px-4 py-3 text-sm">
                      <button
                        onClick={() => {
                          setEditingCategory(category);
                          setShowForm(true);
                        }}
                        className="rounded-md border border-slate-300 px-3 py-1 text-xs font-medium text-slate-600 hover:bg-slate-100"
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDelete(category)}
                        className="rounded-md border border-red-200 bg-red-50 px-3 py-1 text-xs font-medium text-red-600 hover:bg-red-100"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            {categories.length === 0 && (
              <p className="px-4 py-6 text-center text-sm text-slate-500">No categories yet. Create one to get started.</p>
            )}
          </div>
        </div>
      )}
    </PageContainer>
  );
};
