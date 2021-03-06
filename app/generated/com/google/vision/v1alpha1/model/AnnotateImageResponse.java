/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * Modify at your own risk.
 */

package com.google.vision.v1alpha1.model;

/**
 * Response to an image annotation request.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the Cloud Vision API. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class AnnotateImageResponse extends com.google.api.client.json.GenericJson {

  /**
   * If set, represents the error message for the operation. Note that filled-in mage annotations
   * are guaranteed to be correct, even when error is non-empty.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Status error;

  /**
   * If present, face detection completed successfully.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<FaceAnnotation> faceAnnotations;

  /**
   * If present, label detection completed successfully.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<EntityAnnotation> labelAnnotations;

  /**
   * If present, landmark detection completed successfully.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<EntityAnnotation> landmarkAnnotations;

  /**
   * If present, logo detection completed successfully.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<EntityAnnotation> logoAnnotations;

  /**
   * If present, safe-search annotation completed successfully.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private SafeSearchAnnotation safeSearchAnnotation;

  /**
   * If present, suggestion annotation completed successfully.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<EntityAnnotation> suggestionAnnotations;

  /**
   * If present, text (OCR) detection completed successfully.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<EntityAnnotation> textAnnotations;

  /**
   * If set, represents the error message for the operation. Note that filled-in mage annotations
   * are guaranteed to be correct, even when error is non-empty.
   * @return value or {@code null} for none
   */
  public Status getError() {
    return error;
  }

  /**
   * If set, represents the error message for the operation. Note that filled-in mage annotations
   * are guaranteed to be correct, even when error is non-empty.
   * @param error error or {@code null} for none
   */
  public AnnotateImageResponse setError(Status error) {
    this.error = error;
    return this;
  }

  /**
   * If present, face detection completed successfully.
   * @return value or {@code null} for none
   */
  public java.util.List<FaceAnnotation> getFaceAnnotations() {
    return faceAnnotations;
  }

  /**
   * If present, face detection completed successfully.
   * @param faceAnnotations faceAnnotations or {@code null} for none
   */
  public AnnotateImageResponse setFaceAnnotations(java.util.List<FaceAnnotation> faceAnnotations) {
    this.faceAnnotations = faceAnnotations;
    return this;
  }

  /**
   * If present, label detection completed successfully.
   * @return value or {@code null} for none
   */
  public java.util.List<EntityAnnotation> getLabelAnnotations() {
    return labelAnnotations;
  }

  /**
   * If present, label detection completed successfully.
   * @param labelAnnotations labelAnnotations or {@code null} for none
   */
  public AnnotateImageResponse setLabelAnnotations(java.util.List<EntityAnnotation> labelAnnotations) {
    this.labelAnnotations = labelAnnotations;
    return this;
  }

  /**
   * If present, landmark detection completed successfully.
   * @return value or {@code null} for none
   */
  public java.util.List<EntityAnnotation> getLandmarkAnnotations() {
    return landmarkAnnotations;
  }

  /**
   * If present, landmark detection completed successfully.
   * @param landmarkAnnotations landmarkAnnotations or {@code null} for none
   */
  public AnnotateImageResponse setLandmarkAnnotations(java.util.List<EntityAnnotation> landmarkAnnotations) {
    this.landmarkAnnotations = landmarkAnnotations;
    return this;
  }

  /**
   * If present, logo detection completed successfully.
   * @return value or {@code null} for none
   */
  public java.util.List<EntityAnnotation> getLogoAnnotations() {
    return logoAnnotations;
  }

  /**
   * If present, logo detection completed successfully.
   * @param logoAnnotations logoAnnotations or {@code null} for none
   */
  public AnnotateImageResponse setLogoAnnotations(java.util.List<EntityAnnotation> logoAnnotations) {
    this.logoAnnotations = logoAnnotations;
    return this;
  }

  /**
   * If present, safe-search annotation completed successfully.
   * @return value or {@code null} for none
   */
  public SafeSearchAnnotation getSafeSearchAnnotation() {
    return safeSearchAnnotation;
  }

  /**
   * If present, safe-search annotation completed successfully.
   * @param safeSearchAnnotation safeSearchAnnotation or {@code null} for none
   */
  public AnnotateImageResponse setSafeSearchAnnotation(SafeSearchAnnotation safeSearchAnnotation) {
    this.safeSearchAnnotation = safeSearchAnnotation;
    return this;
  }

  /**
   * If present, suggestion annotation completed successfully.
   * @return value or {@code null} for none
   */
  public java.util.List<EntityAnnotation> getSuggestionAnnotations() {
    return suggestionAnnotations;
  }

  /**
   * If present, suggestion annotation completed successfully.
   * @param suggestionAnnotations suggestionAnnotations or {@code null} for none
   */
  public AnnotateImageResponse setSuggestionAnnotations(java.util.List<EntityAnnotation> suggestionAnnotations) {
    this.suggestionAnnotations = suggestionAnnotations;
    return this;
  }

  /**
   * If present, text (OCR) detection completed successfully.
   * @return value or {@code null} for none
   */
  public java.util.List<EntityAnnotation> getTextAnnotations() {
    return textAnnotations;
  }

  /**
   * If present, text (OCR) detection completed successfully.
   * @param textAnnotations textAnnotations or {@code null} for none
   */
  public AnnotateImageResponse setTextAnnotations(java.util.List<EntityAnnotation> textAnnotations) {
    this.textAnnotations = textAnnotations;
    return this;
  }

  @Override
  public AnnotateImageResponse set(String fieldName, Object value) {
    return (AnnotateImageResponse) super.set(fieldName, value);
  }

  @Override
  public AnnotateImageResponse clone() {
    return (AnnotateImageResponse) super.clone();
  }

}
