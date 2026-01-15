import React, { useState } from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { apiService } from '../../services/api';
import type { CreatePostRequest, PostResponse } from '../../types/api';

interface CreatePostFormProps {
  onPostCreated: (post: PostResponse) => void;
}

const validationSchema = yup.object({
  content: yup
    .string()
    .required('Post content is required')
    .min(1, 'Post cannot be empty')
    .max(500, 'Post cannot exceed 500 characters'),
});

const CreatePostForm: React.FC<CreatePostFormProps> = ({ onPostCreated }) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const formik = useFormik<CreatePostRequest>({
    initialValues: {
      status: '',
    },
    validationSchema,
    onSubmit: async (values, { resetForm }) => {
      setLoading(true);
      setError(null);
      try {
        const newPost = await apiService.createPost(values);
        onPostCreated(newPost);
        resetForm();
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to create post');
      } finally {
        setLoading(false);
      }
    },
  });

  const characterCount = formik.values.status.length;
  const maxCharacters = 500;
  const isNearLimit = characterCount > maxCharacters * 0.8;

  return (
    <div className="card mb-4">
      <div className="card-header">
        <h5 className="card-title mb-0">Create New Post</h5>
      </div>
      <div className="card-body">
        {error && (
          <div className="alert alert-danger" role="alert">
            {error}
          </div>
        )}
        <form onSubmit={formik.handleSubmit}>
          <div className="mb-3">
            <textarea
              className={`form-control ${
                formik.touched.status && formik.errors.status
                  ? 'is-invalid'
                  : ''
              }`}
              id="content"
              name="content"
              rows={4}
              placeholder="What's on your mind? You can use hashtags like #technology #social"
              value={formik.values.content}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              disabled={loading}
            />
            {formik.touched.status && formik.errors.status && (
              <div className="invalid-feedback">{formik.errors.status}</div>
            )}
          </div>
          <div className="d-flex justify-content-between align-items-center">
            <small
              className={`text-muted ${
                isNearLimit ? 'text-warning' : ''
              } ${characterCount > maxCharacters ? 'text-danger' : ''}`}
            >
              {characterCount}/{maxCharacters} characters
            </small>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading || !formik.isValid || characterCount === 0}
            >
              {loading ? (
                <>
                  <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                  Posting...
                </>
              ) : (
                'Post'
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreatePostForm;